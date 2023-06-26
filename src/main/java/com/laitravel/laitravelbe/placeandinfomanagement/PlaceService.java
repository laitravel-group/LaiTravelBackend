package com.laitravel.laitravelbe.placeandinfomanagement;


import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.laitravel.laitravelbe.model.Place;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PlaceService {
    final GeoApiContext context;

    public PlaceService(GeoApiContext context) {
       this.context = context;
    }


    public List<PlacesSearchResult> placeSearch(String pla,String startDate,String endDate){
        String cityInfo = String.format("famous travel spots in %s county", pla);
        String pattern = "MM/dd/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate date1 = LocalDate.parse(startDate, formatter);
        LocalDate date2 = LocalDate.parse(endDate, formatter);
        DayOfWeek start = date1.getDayOfWeek();
        DayOfWeek end = date2.getDayOfWeek();

        TextSearchRequest request =  PlacesApi.textSearchQuery(context,cityInfo);

        try {
            PlacesSearchResponse response = request.await();
            PlacesSearchResult[] places = response.results;
            Arrays.sort(places, Comparator.comparing(place -> getRating((PlacesSearchResult) place)).reversed());

            return List.of(places);

//            List<String> placeIds = new ArrayList<>();
//            for(PlacesSearchResult p:places) {
//                placeIds.add(p.placeId);
//            }
//            List<PlaceDetails> results = getPlaceDetails(placeIds);
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



    float getRating(PlacesSearchResult place) {
        return place.rating;
    }


}







