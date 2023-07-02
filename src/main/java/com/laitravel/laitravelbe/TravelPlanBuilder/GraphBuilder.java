package com.laitravel.laitravelbe.TravelPlanBuilder;

import com.laitravel.laitravelbe.model.OpeningHours;
import com.laitravel.laitravelbe.model.Place;
import com.laitravel.laitravelbe.model.PlaceVisitDetails;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphBuilder {

    public List<Place> places = new ArrayList<>();
    public List<Map<Place, Integer>> maps = new ArrayList<>();
    public Map<Place, Map<Place, Integer>> adjacentNodes =  new HashMap<>();
    public Map<Place, PlaceVisitDetails> placeDetails = new HashMap<>();

    public void addPlace(Place place, Map<Place, Integer> neighborMap){
        places.add(place);
        maps.add(neighborMap);
    }

    public void graphBuilder (){
        for (int i = 0; i < places.size(); i++){
            Place place= places.get(i);
            Map<Place, Integer> neighborMap = maps.get(i);
            adjacentNodes.put(place, neighborMap);
        }
    }

    public void buildPlaceVisitDetails(Place place, Integer stayDuration){
        PlaceVisitDetails placeVisitDetails = new PlaceVisitDetails(place, 0, null, null, stayDuration);
        placeDetails.put(place, placeVisitDetails);
    }

    public void setDefault(Map<Place, PlaceVisitDetails> placeDetails) {
        for (PlaceVisitDetails placeVisitDetail : placeDetails.values()) {
            placeVisitDetail.isVisited = false;
            placeVisitDetail.travelTime = Integer.MAX_VALUE;
        }
    }

    int timeToMinutes(Time time) {
        return time.getHours() * 60 + time.getMinutes();
    }

    Time minutesToTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return new Time(hours, mins, 0);
    }

    Time getOpenTime(Place place, String dateTime){
        LocalDate date = LocalDate.parse(dateTime, DateTimeFormatter.ISO_DATE);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int dayOfWeekValue = dayOfWeek.getValue();
        OpeningHours openingHours = place.openingHours().get(dayOfWeekValue - 1);
        return openingHours.openTime();
    }

    Time getCloseTimeTime(Place place, String dateTime){
        LocalDate date = LocalDate.parse(dateTime, DateTimeFormatter.ISO_DATE);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int dayOfWeekValue = dayOfWeek.getValue();
        OpeningHours openingHours = place.openingHours().get(dayOfWeekValue - 1);
        return openingHours.closeTime();
    }

    boolean travelInOpenTime(GraphBuilder graph, PlaceVisitDetails placeDetails, int startTimeInMinutes, String dateTime) {
        int openingTime = graph.timeToMinutes(graph.getOpenTime(placeDetails.place, dateTime));
        int closingTime = graph.timeToMinutes(graph.getCloseTimeTime(placeDetails.place,dateTime));
        int endTimeInMinutes = startTimeInMinutes + placeDetails.stayDuration;

        return startTimeInMinutes >= openingTime && endTimeInMinutes <= closingTime;
    }

    void printCurrentPlaceDetails(PlaceVisitDetails placeVisitDetails) {
        System.err.println(placeVisitDetails.startTime + "-" + placeVisitDetails.endTime + "   Visiting " + placeVisitDetails.place.placeName());
    }

}
