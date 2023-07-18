package com.laitravel.laitravelbe.place;

import com.google.maps.model.*;
import com.laitravel.laitravelbe.api.GoogleCloudService;
import com.laitravel.laitravelbe.api.GooglePlaceService;
import com.laitravel.laitravelbe.db.CityRepository;
import com.laitravel.laitravelbe.db.PlaceRepository;
import com.laitravel.laitravelbe.db.entity.CityEntity;
import com.laitravel.laitravelbe.db.entity.PlaceEntity;
import com.laitravel.laitravelbe.model.OpeningHours;
import com.laitravel.laitravelbe.model.Place;
import com.laitravel.laitravelbe.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class PlaceService {
    final GooglePlaceService googlePlaceApiService;
    final CityRepository cityRepository;
    final PlaceRepository placeRepository;
    final GoogleCloudService googleCloudService;
    final String bucketName;

    public PlaceService(
            GooglePlaceService service,
            CityRepository cityRepository,
            PlaceRepository placeRepository,
            GoogleCloudService googleCloudService,
            @Value("${GCS.bucket-name}")  String bucketName) {

        this.googlePlaceApiService = service;
        this.cityRepository = cityRepository;
        this.placeRepository = placeRepository;
        this.googleCloudService = googleCloudService;
        this.bucketName = bucketName;
    }


    public Pair<String, List<Place>> placeSearch(String cityName, String startDateString, String endDateString) {
        // return result
        List<Place> resultPlaces = new ArrayList<>();

        // find if city in db
        List<CityEntity> cityEntities = cityRepository.findByCityName(cityName);
        CityEntity city;
        if (!cityEntities.isEmpty()) {
            city = cityEntities.get(0);
        } else {
            // store in db
            PlacesSearchResult[] citySearchResult = googlePlaceApiService.textSearchQuery(cityName);
            city = (citySearchResult != null && citySearchResult.length != 0) ?
                    new CityEntity(citySearchResult[0].placeId, citySearchResult[0].name) : null;
            if (city == null) {
                return Pair.of("", resultPlaces);
            }
            if (cityRepository.findByCityId(city.cityId()) == null) {
                cityRepository.insertCity(city.cityId(), city.cityName());
            }
        }

        String searchQuery = String.format("top travel spots in %s and vicinity", cityName);
        // use yyyy-MM-dd format
        LocalDate startDate = DateTimeUtils.dateStringToLocalDate(startDateString);
        LocalDate endDate = DateTimeUtils.dateStringToLocalDate(endDateString);
        // get what weekdays are in this trip
        List<DayOfWeek> dayOfWeekList = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            dayOfWeekList.add(dayOfWeek);
            currentDate = currentDate.plusDays(1);
            if(dayOfWeekList.size() == 7) {
                break;
            }
        }
        // check if the place data and is recent enough (within 3 months)
        List<PlaceEntity> placeEntities = placeRepository.findByCityId(city.cityId());
        if (!placeEntities.isEmpty()) {
            long lastUpdated = placeEntities.get(0).lastUpdated().getTime();
            long now = new Date().getTime();
            long diff = TimeUnit.MILLISECONDS.toDays(now - lastUpdated);
            if (!placeEntities.isEmpty() && diff < 90) {
                return Pair.of(city.cityId(), placeEntities.stream().map(PlaceEntity::toPlace).toList());
            }
        }

        // use api to search places
        PlacesSearchResult[] placesSearchResults = googlePlaceApiService.textSearchQuery(searchQuery);
        if (placesSearchResults == null || placesSearchResults.length == 0) {
            return Pair.of(city.cityId(), resultPlaces);
        }
        // sort place in rating
        Arrays.sort(placesSearchResults, (place1, place2) -> Float.compare(place2.rating, place1.rating));

        for (PlacesSearchResult placesSearchResult : placesSearchResults) {
            PlaceDetails placeDetails = googlePlaceApiService.getPlaceDetails(placesSearchResult.placeId);
            // get description
            String description = placeDetails.editorialSummary != null ?
                    placeDetails.editorialSummary.overview : null;
            // get opening hours
            com.google.maps.model.OpeningHours.Period[] periods = placeDetails.openingHours != null ?
                    placeDetails.openingHours.periods : null;
            // get photo
            byte[] photoData = placeDetails.photos != null ?
                    googlePlaceApiService.getImageByReference(placeDetails.photos[0].photoReference) : null;
            // save photo to gcs
            String mediaLink = googleCloudService.uploadImage(bucketName, placesSearchResult.placeId,photoData);


            // places are added to database regardless valid or not
            List<OpeningHours> openingHours = parseOpeningPeriods(periods);
            Place newPlace = new Place(
                    placesSearchResult.placeId,
                    placesSearchResult.name,
                    city.cityId(),
                    placesSearchResult.geometry.location.lat,
                    placesSearchResult.geometry.location.lng,
                    mediaLink,
                    List.of(placesSearchResult.types),
                    placesSearchResult.formattedAddress,
                    description,
                    placesSearchResult.rating,
                    openingHours);
            PlaceEntity newPlaceEntity = newPlace.toPlaceEntity();
            if (placeRepository.findByPlaceId(newPlace.placeId()) == null) {
                placeRepository.insertPlaceId(newPlaceEntity.placeId(), newPlace.cityId());
            }
            placeRepository.save(newPlaceEntity);


            // if days in dayOfWeekList contains opening hours, add to return result
            if (containsValidWeekDays(dayOfWeekList, openingHours)) {
                resultPlaces.add(newPlace);
            }
        }

        return Pair.of(city.cityId(), resultPlaces);
    }

    public Map<Place, Map<Place,Integer>> getPlaceTravelTimeMap(Place origin, List<Place> destinations) {
        Map<Place,Map<Place,Integer>> answer = new HashMap<>();

        Map<Place,Integer> adj = getMap(origin, destinations);
        answer.put(origin, adj);
        for (int i = 0; i < destinations.size(); i++) {
            List<Place> newList = new ArrayList<>();
            for (int j = 0; j < destinations.size(); j++) {  //destinations.size()
                if (i == j) continue;
                newList.add(destinations.get(j));
            }
            Place start = destinations.get(i);
            adj = getMap(start,newList);
            answer.put(start, adj);
        }
        return answer;
    }

    private Map<Place,Integer> getMap(Place origin,List<Place> destinations) {
        Map<Place,Integer> adj = new HashMap<>();
        LatLng startLocation = new LatLng(origin.lat(), origin.lng());
        for (Place place:destinations) {
            LatLng desLocation = new LatLng(place.lat(), place.lng());
            DistanceMatrixRow[] result = googlePlaceApiService.getDistanceMatrix(startLocation, desLocation).rows;
            if(result.length == 0) return null;
            DistanceMatrixElement[] elements = result[0].elements;
            int minutes = 60;
            if(elements[0].duration != null) {
                minutes = (int) Math.ceil(elements[0].duration.inSeconds/60.0);
            }
            adj.put(place, minutes);

        }
        return adj;
    }

    private boolean containsValidWeekDays(List<DayOfWeek> dayOfWeekList, List<OpeningHours> openingHours) {
        for (OpeningHours dayOpeningHours : openingHours) {
            if (dayOfWeekList.contains(dayOpeningHours.dayOfWeek())) {
                return true;
            }
        }
        return false;
    }

    private List<OpeningHours> parseOpeningPeriods(com.google.maps.model.OpeningHours.Period[] periods) {
        List<com.laitravel.laitravelbe.model.OpeningHours> openingHours = new ArrayList<>();
        // if no period, default opening hours are 9am to 6pm everyday
        if (periods == null) {
            for (int i = 1; i < 7; i++) {
                openingHours.add(new com.laitravel.laitravelbe.model.OpeningHours(
                        DayOfWeek.of(i),
                        "09:00",
                        "18:00"));
            }
        } else {
            for (com.google.maps.model.OpeningHours.Period period : periods) {
                openingHours.add(new com.laitravel.laitravelbe.model.OpeningHours(
                        DayOfWeek.valueOf(period.open.day.getName().toUpperCase()),
                        DateTimeUtils.localTimeToString(period.open.time),
                        period.close != null ?
                                DateTimeUtils.localTimeToString(period.close.time) : "18:00"));
            }
        }
        return openingHours;
    }

}







