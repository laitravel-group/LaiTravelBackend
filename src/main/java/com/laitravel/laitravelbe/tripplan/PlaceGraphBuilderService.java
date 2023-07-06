package com.laitravel.laitravelbe.tripplan;

import com.laitravel.laitravelbe.model.OpeningHours;
import com.laitravel.laitravelbe.model.Place;
import com.laitravel.laitravelbe.model.PlaceVisitDetails;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class PlaceGraphBuilderService {

    public Map<Place, Map<Place, Integer>> adjacentNodes;
    public Map<Place, PlaceVisitDetails> placeDetails;

    public PlaceGraphBuilderService (){
        this.adjacentNodes = new HashMap<>();
        this.placeDetails = new HashMap<>();
    }

    int timeToMinutes(LocalTime time) {
        return time.getHour() * 60 + time.getMinute();
    }

    LocalTime minutesToTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return LocalTime.of(hours, mins, 0);
    }

    LocalTime getOpenTime(Place place, String dateTime){
        LocalDate date = LocalDate.parse(dateTime, DateTimeFormatter.ISO_DATE);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int dayOfWeekValue = dayOfWeek.getValue();
        OpeningHours openingHours = place.openingHours().get(dayOfWeekValue - 1);
        return openingHours.openTime();
    }

    LocalTime getCloseTimeTime(Place place, String dateTime){
        LocalDate date = LocalDate.parse(dateTime, DateTimeFormatter.ISO_DATE);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int dayOfWeekValue = dayOfWeek.getValue();
        OpeningHours openingHours = place.openingHours().get(dayOfWeekValue - 1);
        return openingHours.closeTime();
    }

    boolean travelInOpenTime(PlaceVisitDetails placeDetails, int startTimeInMinutes, String dateTime) {
        int openingTime = timeToMinutes(getOpenTime(placeDetails.place, dateTime));
        int closingTime = timeToMinutes(getCloseTimeTime(placeDetails.place, dateTime));
        int endTimeInMinutes = startTimeInMinutes + placeDetails.stayDuration;

        return startTimeInMinutes >= openingTime && endTimeInMinutes <= closingTime;
    }


    void printCurrentPlaceDetails(PlaceVisitDetails placeVisitDetails) {
        System.err.println(placeVisitDetails.startTime + "-" + placeVisitDetails.endTime + "   Visiting " + placeVisitDetails.place.placeName());
    }

}
