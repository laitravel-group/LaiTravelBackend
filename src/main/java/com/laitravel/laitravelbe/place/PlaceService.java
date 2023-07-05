package com.laitravel.laitravelbe.place;

import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;
import com.laitravel.laitravelbe.model.OpeningHours;
import com.laitravel.laitravelbe.model.Place;
import com.laitravel.laitravelbe.place.api.GooglePlaceApiService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PlaceService {
    final GooglePlaceApiService googlePlaceApiService;

    public PlaceService(GooglePlaceApiService service) {
       this.googlePlaceApiService = service;
    }


    public List<Place> placeSearch(String cityName, String startDateString, String endDateString){
        // TODO
        // find if city in db

        // if none then search the city first
        PlacesSearchResult[] citySearchResult = googlePlaceApiService.textSearchQuery(cityName);


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


        // TODO
        // check if db has data of the city and if the place data is recent enough (within 1 week)




        // use api to search places
        List<Place> resultPlaces = new ArrayList<>();
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
            byte[] photoData = googlePlaceApiService.getImageByReference(placeDetails.photos[0].photoReference);

            // TODO
            // places are added to database regardless valid or not
            List<OpeningHours> openingHours = parseOpeningPeriods(periods);

            // TODO
            // fill in the city id
            if (containsValidWeekDays(dayOfWeekList, openingHours)) {
                resultPlaces.add(new Place(
                        placesSearchResult.placeId,
                        placesSearchResult.name,
                        "",
                        placesSearchResult.geometry.location.lat,
                        placesSearchResult.geometry.location.lng,
                        "",
                        List.of(placesSearchResult.types),
                        placesSearchResult.formattedAddress,
                        description,
                        placesSearchResult.rating,
                        openingHours));
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
                        DayOfWeek.valueOf(period.open.day.getName()),
                        period.open.time,
                        period.close.time));
            }
        }
        return openingHours;
    }

}







