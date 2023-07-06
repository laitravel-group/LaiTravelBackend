package com.laitravel.laitravelbe.tripplan;

import com.laitravel.laitravelbe.model.Place;
import com.laitravel.laitravelbe.model.PlaceVisitDetails;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;
import com.laitravel.laitravelbe.place.PlaceService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class TravelPlanPerDayService {
    final PlaceGraphBuilderService placeGraphBuilderService;

    final PlaceService placeService;

    public TripPlanDetailsPerDay tripPlanDetailsPerDay;

    public TravelPlanPerDayService(PlaceGraphBuilderService placeGraphBuilderService, PlaceService placeService) {

        this.placeGraphBuilderService = placeGraphBuilderService;
        this.placeService = placeService;
    }

    public void getDistance( Place origin){
        List<Place> destinations = new ArrayList<>();
        for (PlaceVisitDetails visit : tripPlanDetailsPerDay.visits()) {
            if (!visit.place.equals(origin)){
                destinations.add(visit.place);
            }
        }
        placeGraphBuilderService.adjacentNodes = placeService.getDistances(origin, destinations);
    }

    public TripPlanDetailsPerDay calculateShortestPath(Place start, Date date, LocalTime startTime, LocalTime endTime) {
        getDistance(start);

        // Priority queue to store unvisited places, prioritized by travel time
        PriorityQueue<PlaceVisitDetails> unVisitedPlaces = new PriorityQueue<>(Comparator.comparingLong(c -> c.travelTime));
        // Store unvisited places
        Set<PlaceVisitDetails> unVisitedPlaceSet = new HashSet<>();

        // Get the details of the start place
        PlaceVisitDetails startPlaceDetails = placeGraphBuilderService.placeDetails.get(start);

        // Check if the start place is open, if not, prompt the user
        if (!placeGraphBuilderService.travelInOpenTime(startPlaceDetails, placeGraphBuilderService.timeToMinutes(startTime), date)) {
            System.err.println("The place you chose is not open yet, please change start time or start location!");
            return tripPlanDetailsPerDay;
        }

        // Add the start place to the unvisited queue and set
        unVisitedPlaces.add(startPlaceDetails);
        unVisitedPlaceSet.add(startPlaceDetails);

        // Set the current time to the start time
        LocalTime currentTime = startTime;
        // Calculate the end time in minutes
        int endTimeInMinutes = placeGraphBuilderService.timeToMinutes(endTime);

        // Process each travel plan in the queue
        while (!unVisitedPlaces.isEmpty()) {
            // Fetch the travel plan from the queue and immediately remove it
            PlaceVisitDetails currentPlaceDetails = unVisitedPlaces.poll();
            // Calculate the arrival time and leave time
            int arrivalTimeInMinutes = placeGraphBuilderService.timeToMinutes(currentTime) + currentPlaceDetails.travelTime;
            int leaveTimeInMinutes = arrivalTimeInMinutes + currentPlaceDetails.stayDuration;

            // If the place is not open or if the stay duration exceeds the limit, skip this place
            if (!placeGraphBuilderService.travelInOpenTime(currentPlaceDetails, arrivalTimeInMinutes, date) || leaveTimeInMinutes > endTimeInMinutes) {
                continue;
            }

            // Mark the current place as visited and set the start and end time
            currentPlaceDetails.isVisited = true;
            currentPlaceDetails.startTime = placeGraphBuilderService.minutesToTime(arrivalTimeInMinutes);
            currentPlaceDetails.endTime = placeGraphBuilderService.minutesToTime(leaveTimeInMinutes);
            // Print the current place details
            placeGraphBuilderService.printCurrentPlaceDetails(currentPlaceDetails);

            // Add the current place to the final plans
            tripPlanDetailsPerDay.visits().add(currentPlaceDetails);
            // Update the current time to the end time of the current place
            currentTime = currentPlaceDetails.endTime;

            // Iterate over the adjacent nodes of the current node
            for (Map.Entry<Place, Integer> entry : placeGraphBuilderService.adjacentNodes.get(currentPlaceDetails.place).entrySet()) {
                // Get the details of the adjacent place
                PlaceVisitDetails adjacentPlaceDetails = placeGraphBuilderService.placeDetails.get(entry.getKey());
                // Update the travel time to the adjacent place
                adjacentPlaceDetails.travelTime = entry.getValue();

                // If the adjacent place is not visited and not in the unvisited set, add it to the queue and set
                if (!adjacentPlaceDetails.isVisited && !unVisitedPlaceSet.contains(adjacentPlaceDetails)) {
                    unVisitedPlaces.offer(adjacentPlaceDetails);
                    unVisitedPlaceSet.add(adjacentPlaceDetails);
                }
            }
        }

        return tripPlanDetailsPerDay;
    }

    public TripPlanDetailsPerDay autoPath(List<PlaceVisitDetails> visits, Date date, LocalTime startTime, LocalTime endTime) {
        LocalTime currentTime = startTime;
        PlaceVisitDetails prevPlaceVisitDetails = null;

        for (PlaceVisitDetails visit : visits) {
            int arrivalTimeInMinutes = placeGraphBuilderService.timeToMinutes(currentTime) + prevPlaceVisitDetails.travelTime;
            int leaveTimeInMinutes = arrivalTimeInMinutes + prevPlaceVisitDetails.stayDuration;
            int travelTime = placeGraphBuilderService.adjacentNodes.get(prevPlaceVisitDetails.place).get(visit.place);

            // If the place is not open or if the stay duration exceeds the limit, remind the user
            if (!placeGraphBuilderService.travelInOpenTime(prevPlaceVisitDetails, arrivalTimeInMinutes, date) || leaveTimeInMinutes > placeGraphBuilderService.timeToMinutes(endTime)) {
                System.err.println("The trip plan per day is not working because " + visit.place.placeName() + " visit time is not available for open time or close time or day endtime");
            }

            visit.startTime = placeGraphBuilderService.minutesToTime(arrivalTimeInMinutes);
            visit.endTime = placeGraphBuilderService.minutesToTime(leaveTimeInMinutes);
            visit.travelTime = travelTime;

            visits.add(visit);
            currentTime = visit.endTime;
            prevPlaceVisitDetails = visit;
        }

        return tripPlanDetailsPerDay;
    }

}
