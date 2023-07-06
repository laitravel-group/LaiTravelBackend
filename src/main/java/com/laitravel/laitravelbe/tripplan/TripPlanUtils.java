package com.laitravel.laitravelbe.tripplan;

import com.laitravel.laitravelbe.model.OpeningHours;
import com.laitravel.laitravelbe.model.Place;
import com.laitravel.laitravelbe.model.PlaceVisitDetails;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TripPlanUtils {
    static int timeToMinutes(LocalTime time) {
        return time.getHour() * 60 + time.getMinute();
    }

    static LocalTime minutesToTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return LocalTime.of(hours, mins, 0);
    }


    // DEBUG
    static void printCurrentPlaceDetails(PlaceVisitDetails placeVisitDetails) {
        System.err.println(placeVisitDetails.startTime + "-" + placeVisitDetails.endTime + "   Visiting " + placeVisitDetails.place.placeName());
    }
}
