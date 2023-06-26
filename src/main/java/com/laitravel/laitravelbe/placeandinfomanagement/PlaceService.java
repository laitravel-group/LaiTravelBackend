package com.laitravel.laitravelbe.placeandinfomanagement;


import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.laitravel.laitravelbe.model.Place;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.DayOfWeek;

import java.io.IOException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class PlaceService {
    final GeoApiContext context;

    public PlaceService(GeoApiContext context) {
       this.context = context;
    }


    public List<Place> placeSearch(String pla,String startDate,String endDate){
        String cityInfo = String.format("famous travel spots in %s county", pla);
        String pattern = "MM/dd/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate startDay = LocalDate.parse(startDate, formatter);
        LocalDate endDay = LocalDate.parse(endDate, formatter);
        List<String> dayOfWeekList = new ArrayList<>();
        LocalDate currentDate = startDay;
        while (!currentDate.isAfter(endDay)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            String dayOfWeekString = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
            dayOfWeekList.add(dayOfWeekString);
            currentDate = currentDate.plusDays(1);
            if(dayOfWeekList.size() == 7) {
                break;
            }
        }




        TextSearchRequest request =  PlacesApi.textSearchQuery(context,cityInfo);

        try {
            PlacesSearchResponse response = request.await();
            PlacesSearchResult[] places = response.results;

// sort place in rating

            Arrays.sort(places, Comparator.comparing(place -> getRating((PlacesSearchResult) place)).reversed());

// customize place detail request
            PlaceDetailsRequest detailsRequest = new PlaceDetailsRequest(context)
                    .fields(PlaceDetailsRequest.FieldMask.OPENING_HOURS, PlaceDetailsRequest.FieldMask.EDITORIAL_SUMMARY);

//  maybe we can use this placeIds to get place info from our database
            List<String> placeIds = new ArrayList<>();


// return list of Places
            List<Place> resultPlaces = new ArrayList<>();


            for(PlacesSearchResult p:places) {
                placeIds.add(p.placeId);
                PlaceDetailsRequest detailsRequest1 = detailsRequest.placeId(p.placeId);
                PlaceDetails result = detailsRequest1.await();
                OpeningHours.Period[] periods = result.openingHours.periods;
                String summary = result.editorialSummary.overview;
                if(isValidDate(dayOfWeekList,periods) == false) {
                    continue;
                }
                List<com.laitravel.laitravelbe.model.OpeningHours> openingHours = null;
                for(OpeningHours.Period oneOperiod : periods) {
                    com.laitravel.laitravelbe.model.OpeningHours open = new com.laitravel.laitravelbe.model.OpeningHours(DayOfWeek.valueOf(oneOperiod.open.day.name()), Time.valueOf(oneOperiod.open.time),Time.valueOf(oneOperiod.close.time));
                    openingHours.add(open);
                }
                Place resultplace = new Place(p.placeId,p.name,p.geometry.location.lat,p.geometry.location.lng,"",List.of(p.types),p.formattedAddress,summary,openingHours);
                resultPlaces.add(resultplace);
            }


            return resultPlaces;

//
//
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

    }


    public List<PlaceDetails> getPlaceDetails (List<String> placeIds) {
        List<PlaceDetails> result = new ArrayList<>();
        for(String placeId : placeIds) {
            PlaceDetailsRequest request = PlacesApi.placeDetails(context, placeId);
            try {
                PlaceDetails requestResult = request.await();
                result.add(requestResult);

            } catch (ApiException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
    public GeocodingResult[] geoSearch(String address) {
        GeocodingApiRequest request = GeocodingApi.geocode(context,address);
        try {
            GeocodingResult[] results = request.await();
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public GeocodingResult[] geoSearchPlaceID(String PlaceID) {
        GeocodingApiRequest request = GeocodingApi.newRequest(context)
                .place(PlaceID);
        try {
            GeocodingResult[] results = request.await();
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getImageByText(String photoReference) {
        PhotoRequest request = new PhotoRequest(context);
        try {
            ImageResult image = request.maxHeight(800).maxWidth(600).photoReference(photoReference).await();
            return image.imageData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DistanceMatrix getDistance(String[] origin, String[] dest) {

        DistanceMatrixApiRequest request = DistanceMatrixApi.getDistanceMatrix(context,origin,dest);
        try {
            DistanceMatrix result = request.await();
            return result;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    boolean isValidDate(List<String> dayOfWeekList,OpeningHours.Period[] periods ) {
        boolean result = false;
        for(String day : dayOfWeekList) {
            boolean found = false;
            for(OpeningHours.Period oneOperiod : periods) {
                if(day.equals(oneOperiod.open.day.getName())) {
                    found = true;
                    result = true;
                    break;
                }
            }
            if (found) break;
        }
        return result;
    }

    float getRating(PlacesSearchResult place) {
        return place.rating;
    }


}







