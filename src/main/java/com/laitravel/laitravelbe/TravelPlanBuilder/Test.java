package com.laitravel.laitravelbe.TravelPlanBuilder;

import com.google.maps.model.OpeningHours;
import com.laitravel.laitravelbe.model.OpeningHours;
import com.laitravel.laitravelbe.model.Place;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {

        GraphBuilder plan = new GraphBuilder();

        // get information
        List<OpeningHours> openingHoursA = new ArrayList<>();
        OpeningHours openingHourA = new OpeningHours(DayOfWeek.MONDAY, Time.valueOf("06:00:00"), Time.valueOf("18:00:00"));
        openingHoursA.add(openingHourA);
        Place A = new Place("A", "Place A", 8.0f, 18.0f, "", new ArrayList<>(), "", "", openingHoursA);
        plan.buildPlaceVisitDetails(A, 240);

        List<OpeningHours> openingHoursB = new ArrayList<>();
        OpeningHours openingHourB = new OpeningHours(DayOfWeek.MONDAY, Time.valueOf("10:00:00"), Time.valueOf("22:00:00"));
        openingHoursB.add(openingHourB);
        Place B = new Place("B", "Place B", 10.0f, 22.0f, "", new ArrayList<>(), "", "", openingHoursB);
        plan.buildPlaceVisitDetails(B, 240);

        List<OpeningHours> openingHoursC = new ArrayList<>();
        OpeningHours openingHourC = new OpeningHours(DayOfWeek.MONDAY, Time.valueOf("12:00:00"), Time.valueOf("23:00:00"));
        openingHoursC.add(openingHourC);
        Place C = new Place("C", "Place C", 12.0f, 17.0f, "", new ArrayList<>(), "", "", openingHoursC);
        plan.buildPlaceVisitDetails(C, 240);

        List<OpeningHours> openingHoursD = new ArrayList<>();
        OpeningHours openingHourD = new OpeningHours(DayOfWeek.MONDAY, Time.valueOf("14:00:00"), Time.valueOf("23:59:59"));
        openingHoursD.add(openingHourD);
        Place D = new Place("D", "Place D", 14.0f, 24.0f, "", new ArrayList<>(), "", "", openingHoursD);
        plan.buildPlaceVisitDetails(D, 120);

        // get information

        Map<Place, Integer> mapA = new HashMap<>();
        mapA.put(B, 18);
        mapA.put(C, 24);
        mapA.put(D, 42);

        Map<Place, Integer> mapB = new HashMap<>();
        mapB.put(A, 18);
        mapB.put(C, 24);
        mapB.put(D, 30);

        Map<Place, Integer> mapC = new HashMap<>();
        mapC.put(A, 24);
        mapC.put(B, 24);
        mapC.put(D, 48);

        Map<Place, Integer> mapD = new HashMap<>();
        mapD.put(A, 42);
        mapD.put(B, 30);
        mapD.put(C, 48);

        plan.addPlace(A, mapA);
        plan.addPlace(B, mapB);
        plan.addPlace(C, mapC);
        plan.addPlace(D, mapD);

        plan.graphBuilder();

        com.example.laitravel0626.TravelPlanBuilder.TravelPlanService path1 = new com.example.laitravel0626.TravelPlanBuilder.TravelPlanService(plan);
        System.out.println("shortest path:");
        String dateTime = "2023-06-26";
        Time startTime = Time.valueOf("6:00:00");
        Time endTime = Time.valueOf("23:59:00");
        path1.calculateShortestPath( A, dateTime, startTime, endTime);

    }

}
