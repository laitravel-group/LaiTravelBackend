package com.laitravel.laitravelbe.tripplan;

import com.laitravel.laitravelbe.model.Place;
import com.laitravel.laitravelbe.model.PlaceVisitDetails;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;
import com.laitravel.laitravelbe.util.DateTimeUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class TripPlanBuilder {
    private final PlaceVisitOrderHelper placeVisitOrderHelper;
    public TripPlanDetailsPerDay desiredPlan;

    public TripPlanBuilder(TripPlanDetailsPerDay desiredPlan, Map<Place, Map<Place, Integer>> placeTravelTimeMap) {
        this.desiredPlan = desiredPlan;
        placeVisitOrderHelper = new PlaceVisitOrderHelper(placeTravelTimeMap);
        // Added all places to the map
        desiredPlan.visits().forEach(placeVisitDetails ->
                placeVisitOrderHelper.placeToDetailsMap.put(placeVisitDetails.place, placeVisitDetails));
    }


    public TripPlanDetailsPerDay calculateShortestPath() {
        LocalDate date = LocalDate.parse(desiredPlan.date());
        Place startLocation = desiredPlan.startLocation();
        LocalTime startTime = desiredPlan.startTime() == null ? LocalTime.of(9,0) :
                DateTimeUtils.timeStringToLocalTime(desiredPlan.startTime());
        LocalTime endTime = desiredPlan.endTime() == null ? LocalTime.of(18,0) :
                DateTimeUtils.timeStringToLocalTime(desiredPlan.endTime());

        // List of place visit details for result
        List<PlaceVisitDetails> proposedDetails = new LinkedList<>();

        // initialise and update travel time for start location
        placeVisitOrderHelper.updateTravelTime(startLocation);

        // Set the current time to the start time
        LocalTime currentTime = startTime;
        // Process each travel plan in the queue
        while (!placeVisitOrderHelper.unvisitedPlacePQ.isEmpty()) {
            // Fetch the travel plan from the queue and immediately remove it
            PlaceVisitDetails currentPlaceDetails = placeVisitOrderHelper.unvisitedPlacePQ.poll();
            placeVisitOrderHelper.unvisitedPlaceSet.remove(currentPlaceDetails);
            // Calculate the arrival time and leave time
            LocalTime arrivalTime = currentTime.plusMinutes(currentPlaceDetails.travelTime);
            LocalTime leaveTime = arrivalTime.plusMinutes(currentPlaceDetails.stayDuration);

            // If the place is not open or if the stay duration exceeds the limit, skip this place
            if (currentPlaceDetails.canVisitAtDateAndTime(date, arrivalTime)
                    && !leaveTime.isAfter(endTime)) {
                currentPlaceDetails.isVisited = true;
                currentPlaceDetails.startTime = DateTimeUtils.localTimeToString(arrivalTime);
                currentPlaceDetails.endTime = DateTimeUtils.localTimeToString(leaveTime);

                // DEBUG: Print the current place details
                //TripPlanUtils.printCurrentPlaceDetails(currentPlaceDetails);

                // Add the current place to the final plans
                proposedDetails.add(currentPlaceDetails);

                // Update the current time to the end time of the current place
                currentTime = leaveTime;
                placeVisitOrderHelper.updateTravelTime(currentPlaceDetails.place);
            }
        }

        return new TripPlanDetailsPerDay(DateTimeUtils.localDateToString(date), startLocation,
                DateTimeUtils.localTimeToString(startTime),
                DateTimeUtils.localTimeToString(endTime), proposedDetails);
    }

    public TripPlanDetailsPerDay autoPath() {
//        LocalDate date = LocalDate.parse(desiredPlan.date());
//        LocalTime startTime = DateTimeUtils.timeStringToLocalTime(desiredPlan.startTime());
//        LocalTime endTime = DateTimeUtils.timeStringToLocalTime(desiredPlan.endTime());
//
//        List<PlaceVisitDetails> visits = desiredPlan.visits();
//        LocalTime currentTime = startTime;
//        PlaceVisitDetails prevPlaceVisitDetails = null;
//
//        for (PlaceVisitDetails visit : visits) {
//            int arrivalTimeInMinutes = TripPlanUtils.timeToMinutes(currentTime) + prevPlaceVisitDetails.travelTime;
//            int leaveTimeInMinutes = arrivalTimeInMinutes + prevPlaceVisitDetails.stayDuration;
//            int travelTime = placeVisitOrderHelper.adjacentNodes.get(prevPlaceVisitDetails.place).get(visit.place);
//
//            // If the place is not open or if the stay duration exceeds the limit, remind the user
//            if (!placeVisitOrderHelper.canVisitAtDateAndTime(prevPlaceVisitDetails, date, arrivalTimeInMinutes) || leaveTimeInMinutes > TripPlanUtils.timeToMinutes(endTime)) {
//                System.err.println("The trip plan per day is not working because " + visit.place.placeName() + " visit time is not available for open time or close time or day endtime");
//            }
//
//            visit.startTime = TripPlanUtils.minutesToTime(arrivalTimeInMinutes);
//            visit.endTime = TripPlanUtils.minutesToTime(leaveTimeInMinutes);
//            visit.travelTime = travelTime;
//
//            visits.add(visit);
//            currentTime = visit.endTime;
//            prevPlaceVisitDetails = visit;
//        }
//
//        return desiredPlan;
        return null;
    }

    private class PlaceVisitOrderHelper {
        public Map<Place, Map<Place, Integer>> placeTravelTimeMap;

        // Use to map Place back to PlaceVisitDetails
        public Map<Place, PlaceVisitDetails> placeToDetailsMap;

        // Priority queue to store unvisited places, prioritized by travel time
        PriorityQueue<PlaceVisitDetails> unvisitedPlacePQ;

        // Store unvisited places
        Set<PlaceVisitDetails> unvisitedPlaceSet;

        private PlaceVisitOrderHelper(Map<Place, Map<Place, Integer>> placeTravelTimeMap){
            this.placeTravelTimeMap = placeTravelTimeMap;
            this.placeToDetailsMap = new HashMap<>();
            this.unvisitedPlacePQ = new PriorityQueue<>(Comparator.comparingLong(c -> c.travelTime));
            this.unvisitedPlaceSet = new HashSet<>();
        }

        private void updateTravelTime(Place place) {
            for (Map.Entry<Place, Integer> entry : placeTravelTimeMap.get(place).entrySet()) {
                // Get the details of the adjacent places
                PlaceVisitDetails adjacentPlaceDetails = placeToDetailsMap.get(entry.getKey());
                if (!adjacentPlaceDetails.isVisited && !unvisitedPlaceSet.contains(adjacentPlaceDetails)) {
                    // Update the travel time to the adjacent place
                    adjacentPlaceDetails.travelTime = entry.getValue();
                    unvisitedPlacePQ.offer(adjacentPlaceDetails);
                    unvisitedPlaceSet.add(adjacentPlaceDetails);
                }
            }
        }
    }
}
