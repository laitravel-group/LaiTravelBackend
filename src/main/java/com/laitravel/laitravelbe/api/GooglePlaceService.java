package com.laitravel.laitravelbe.api;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GooglePlaceService {
    final GeoApiContext context;

    public GooglePlaceService(GeoApiContext context) {
        this.context = context;
    }

    public PlacesSearchResult[] textSearchQuery(String searchQuery) {
        TextSearchRequest request = PlacesApi.textSearchQuery(context, searchQuery);
        try {
            PlacesSearchResponse response = request.await();
            return response.results;
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PlaceDetails getPlaceDetails(String placeId) {
        try {
            return PlacesApi.placeDetails(context, placeId)
                    .fields(PlaceDetailsRequest.FieldMask.OPENING_HOURS, PlaceDetailsRequest.FieldMask.EDITORIAL_SUMMARY)
                    .await();
        } catch (ApiException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public GeocodingResult[] geoSearch(String address) {
        GeocodingApiRequest request = GeocodingApi.geocode(context,address);
        try {
            return request.await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GeocodingResult[] geoSearchPlaceID(String PlaceID) {
        GeocodingApiRequest request = GeocodingApi.newRequest(context)
                .place(PlaceID);
        try {
            return request.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getImageByReference(String photoReference) {
        PhotoRequest request = new PhotoRequest(context);
        try {
            ImageResult image = request.maxHeight(800).maxWidth(600).photoReference(photoReference).await();
            return image.imageData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DistanceMatrix getDistances(String[] origin, String[] dest) {
        DistanceMatrixApiRequest request = DistanceMatrixApi.getDistanceMatrix(context,origin,dest);
        try {
            return request.await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DistanceMatrix getDistanceMatrix(LatLng origin, LatLng destination) {
        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(context)
                .origins(origin)
                .destinations(destination);
        try {
            return request.await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }



}
