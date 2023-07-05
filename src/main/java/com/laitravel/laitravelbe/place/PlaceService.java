package com.laitravel.laitravelbe.place;

import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;
import com.laitravel.laitravelbe.db.CityRepository;
import com.laitravel.laitravelbe.db.PlaceRepository;
import com.laitravel.laitravelbe.db.entity.CityEntity;
import com.laitravel.laitravelbe.db.entity.PlaceEntity;
import com.laitravel.laitravelbe.model.OpeningHours;
import com.laitravel.laitravelbe.model.Place;
import com.laitravel.laitravelbe.api.GooglePlaceService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PlaceService {
    final GooglePlaceService googlePlaceApiService;
    final CityRepository cityRepository;
    final PlaceRepository placeRepository;

    public PlaceService(GooglePlaceService service, CityRepository cityRepository, PlaceRepository placeRepository) {
        this.googlePlaceApiService = service;
        this.cityRepository = cityRepository;
        this.placeRepository = placeRepository;
    }


    public List<Place> placeSearch(String cityName, String startDateString, String endDateString) {
        // return result
        List<Place> resultPlaces = new ArrayList<>();

        // find if city in db
        List<CityEntity> cityEntities = cityRepository.findByCityName(cityName);
        CityEntity city = null;
        if (!cityEntities.isEmpty()) {
            city = cityEntities.get(0);
        } else {
            // store in db
            PlacesSearchResult[] citySearchResult = googlePlaceApiService.textSearchQuery(cityName);
            city = (citySearchResult != null && citySearchResult.length != 0) ?
                    new CityEntity(citySearchResult[0].placeId, citySearchResult[0].name) : null;
            if (city != null && cityRepository.findByCityId(city.cityId()) == null) {
                cityRepository.insertCity(city.cityId(), city.cityName());
            } else {
                return resultPlaces;
            }
        }

        String searchQuery = String.format("famous travel spots in %s county", cityName);
        // use US standard date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalDate endDate = LocalDate.parse(endDateString, formatter);
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
                return placeEntities.stream().map(PlaceEntity::toPlace).toList();
            }
        }


        // use api to search places
        PlacesSearchResult[] placesSearchResults = googlePlaceApiService.textSearchQuery(searchQuery);
        if (placesSearchResults == null || placesSearchResults.length == 0) {
            return resultPlaces;
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
            // TODO
            // save photo to gcs
            byte[] photoData = placeDetails.photos != null ?
                    googlePlaceApiService.getImageByReference(placeDetails.photos[0].photoReference) : null;


            // places are added to database regardless valid or not
            List<OpeningHours> openingHours = parseOpeningPeriods(periods);
            Place newPlace = new Place(
                    placesSearchResult.placeId,
                    placesSearchResult.name,
                    city.cityId(),
                    placesSearchResult.geometry.location.lat,
                    placesSearchResult.geometry.location.lng,
                    "",
                    List.of(placesSearchResult.types),
                    placesSearchResult.formattedAddress,
                    description,
                    placesSearchResult.rating,
                    openingHours);
            PlaceEntity newPlaceEntity = newPlace.toPlaceEntity();
            placeRepository.insertPlace(newPlaceEntity.placeId(), newPlaceEntity.placeName(),
                    newPlaceEntity.cityId(), newPlaceEntity.lat(),
                    newPlaceEntity.lng(), newPlaceEntity.photo(), newPlaceEntity.types(),
                    newPlaceEntity.formattedAddress(), newPlaceEntity.description(),
                    newPlaceEntity.rating(), newPlaceEntity.openingHours(), newPlaceEntity.lastUpdated());

            // if days in dayOfWeekList contains opening hours, add to return result
            if (containsValidWeekDays(dayOfWeekList, openingHours)) {
                resultPlaces.add(newPlace);
            }
        }

        return resultPlaces;
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
                        LocalTime.of(9, 0),
                        LocalTime.of(18, 0)));
            }
        } else {
            for (com.google.maps.model.OpeningHours.Period period : periods) {
                openingHours.add(new com.laitravel.laitravelbe.model.OpeningHours(
                        DayOfWeek.valueOf(period.open.day.getName().toUpperCase()),
                        period.open.time,
                        period.close != null ? period.close.time : LocalTime.of(18, 0)));
            }
        }
        return openingHours;
    }

}







