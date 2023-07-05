package com.laitravel.laitravelbe.placeandinfomanagement;


import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.laitravel.laitravelbe.gcs.GCSService;
import com.laitravel.laitravelbe.model.Place;
import org.springframework.beans.factory.annotation.Value;
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
    final GCSService gcsService;

    final String bucketName;

    public PlaceService(GeoApiContext context, GCSService gcsService,@Value("${GCS.bucket-name}") String bucketName) {
       this.context = context;
       this.gcsService = gcsService;
       this.bucketName = bucketName;
    }


    public List<Place> placeSearch(String pla,String startDate,String endDate){
        String cityInfo = String.format("famous travel spots in %s county and vicinity", pla);
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


// return list of Places
            List<Place> resultPlaces = new ArrayList<>();

            for(PlacesSearchResult p:places) {
//   we should use this placeIds to get detail info from our database
//                if(db.contains(p.placeId)) {
//                   Place dbPlace = db.get(p.placeID）;
//                   resultPlaces.add(dbPlace);
//                     continue;
//                }
                PlaceDetails pl = PlacesApi.placeDetails(context,p.placeId)
                        .fields(PlaceDetailsRequest.FieldMask.OPENING_HOURS, PlaceDetailsRequest.FieldMask.EDITORIAL_SUMMARY,PlaceDetailsRequest.FieldMask.PHOTOS)
                        .await();

                byte[] photo = getPhoto(pl);
                String mediaLink = gcsService.uploadImage(bucketName,p.placeId, photo);

//   assign overview to description
                String description = null;
                if(pl.editorialSummary != null) {
                    description = pl.editorialSummary.overview;
                }
// assign openingHours to periods
                OpeningHours oH = pl.openingHours;
                OpeningHours.Period[] periods = null;
                if(oH != null) {
                    periods = oH.periods;
                }
                if(isValidDate(dayOfWeekList,periods) == false) {
                    continue;
                }
                List<com.laitravel.laitravelbe.model.OpeningHours> open = new ArrayList<>();

                if(periods != null) {
                   for(OpeningHours.Period oneOperiod : periods) {
                       if(oneOperiod.open == null || oneOperiod.close == null) {
                           break;
                       }
                       open.add(new com.laitravel.laitravelbe.model.OpeningHours(DayOfWeek.valueOf(oneOperiod.open.day.name()), Time.valueOf(oneOperiod.open.time), Time.valueOf(oneOperiod.close.time)));
                   }
                }

                Place resultplace = new Place(p.placeId,p.name,p.geometry.location.lat,p.geometry.location.lng,mediaLink,List.of(p.types),p.formattedAddress,description,open,p.rating);
                resultPlaces.add(resultplace);
            }

            return resultPlaces;

        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

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
    public GeocodingResult[] geoSearchByPlaceID(String PlaceID) {
        GeocodingApiRequest request = GeocodingApi.newRequest(context)
                .place(PlaceID);
        try {
            GeocodingResult[] results = request.await();
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getPhoto (PlaceDetails place) {
        if(place == null) return null;
        Photo[] photos = place.photos;
        if (photos == null) {
            return null;
        }
        String photoReference = photos[0].photoReference;
        byte[] photo = getImageByText(photoReference);

        return photo;
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


    public Map<Place,Map<Place,Integer>> getDistances(Place origin, List<Place> destinations) {
        Map<Place,Map<Place,Integer>> answer = new HashMap<>();

        destinations.add(0,origin);

        for(int i=0; i < destinations.size();i++) {
            List<Place> newList = new ArrayList<>();
            for(int j=0; j < destinations.size();j++) {  //destinations.size()
                if(i == j) continue;
                newList.add(destinations.get(j));
            }
            Place start = destinations.get(i);
            Map<Place,Integer> adj = getMap(start,newList);
            answer.put(start,adj);
        }
        return answer;
    }

    Map<Place,Integer> getMap(Place origin,List<Place> destinations) {
        Map<Place,Integer> adj = new HashMap<>();
        LatLng startLocation = new LatLng(origin.lat(), origin.lgt());
        for(Place place:destinations) {
            LatLng desLocation = new LatLng(place.lat(), place.lgt());
            DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(context)
                    .origins(startLocation)
                    .destinations(desLocation);
            try {
                DistanceMatrixRow[] result = request.await().rows;
                if(result.length == 0) return null;
                DistanceMatrixElement[] elements = result[0].elements;
                Integer minutes = 60;
                    if(elements[0].duration != null) {
                        minutes = (int) Math.ceil(elements[0].duration.inSeconds/60.0);
                    }
                    adj.put(place, minutes);
            } catch (ApiException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return adj;





    }



    boolean isValidDate(List<String> dayOfWeekList,OpeningHours.Period[] periods ) {
        boolean result = false;
        if(periods == null) {
            return true;
        }
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







