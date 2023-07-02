package com.example.laitravel0626.TravelPlanBuilder;

import com.example.laitravel0626.Place;
import com.example.laitravel0626.PlaceVisitDetails;
import com.example.laitravel0626.TripPlanDetailsPerDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.*;

@Service
class TravelPlanService {

    public GraphBuilder graphBuilder;
    public TripPlanDetailsPerDay tripPlanDetailsPerDay;
    List<PlaceVisitDetails> visits = tripPlanDetailsPerDay.visits();

    @Autowired
    public TravelPlanService(GraphBuilder graphBuilder, TripPlanDetailsPerDay tripPlanDetailsPerDay) {

        this.graphBuilder = graphBuilder;
        this.tripPlanDetailsPerDay = tripPlanDetailsPerDay;
    }

    public void calculateShortestPath(Place start, String dateTime, Time startTime, Time endTime) {

        // Priority queue to store unvisited places, prioritized by travel time
        PriorityQueue<PlaceVisitDetails> unVisitedPlaces = new PriorityQueue<>(Comparator.comparingLong(c -> c.travelTime));
        // Store unvisited places
        Set<PlaceVisitDetails> unVisitedPlaceSet = new HashSet<>();

        // Get the details of the start place
        PlaceVisitDetails startPlaceDetails = graphBuilder.placeDetails.get(start);

        // Check if the start place is open, if not, prompt the user
        if (!graphBuilder.travelInOpenTime(graphBuilder, startPlaceDetails, graphBuilder.timeToMinutes(startTime), dateTime)) {
            System.err.println("The place you chose is not open yet, please change start time or start location!");
            return;
        }

        // Add the start place to the unvisited queue and set
        unVisitedPlaces.add(startPlaceDetails);
        unVisitedPlaceSet.add(startPlaceDetails);

        // Set the current time to the start time
        Time currentTime = startTime;
        // Calculate the end time in minutes
        int endTimeInMinutes = graphBuilder.timeToMinutes(endTime);

        // Process each travel plan in the queue
        while (!unVisitedPlaces.isEmpty()) {
            // Fetch the travel plan from the queue and immediately remove it
            PlaceVisitDetails currentPlaceDetails = unVisitedPlaces.poll();
            // Calculate the arrival time and leave time
            int arrivalTimeInMinutes = graphBuilder.timeToMinutes(currentTime) + currentPlaceDetails.travelTime;
            int leaveTimeInMinutes = arrivalTimeInMinutes + currentPlaceDetails.stayDuration;

            // If the place is not open or if the stay duration exceeds the limit, skip this place
            if (!graphBuilder.travelInOpenTime(graphBuilder, currentPlaceDetails, arrivalTimeInMinutes, dateTime) || leaveTimeInMinutes > endTimeInMinutes) {
                continue;
            }

            // Mark the current place as visited and set the start and end time
            currentPlaceDetails.isVisited = true;
            currentPlaceDetails.startTime = graphBuilder.minutesToTime(arrivalTimeInMinutes);
            currentPlaceDetails.endTime = graphBuilder.minutesToTime(leaveTimeInMinutes);
            // Print the current place details
            graphBuilder.printCurrentPlaceDetails(currentPlaceDetails);

            // Add the current place to the final plans
            visits.add(currentPlaceDetails);
            // Update the current time to the end time of the current place
            currentTime = currentPlaceDetails.endTime;

            // Iterate over the adjacent nodes of the current node
            for (Map.Entry<Place, Integer> entry : graphBuilder.adjacentNodes.get(currentPlaceDetails.place).entrySet()) {
                // Get the details of the adjacent place
                PlaceVisitDetails adjacentPlaceDetails = graphBuilder.placeDetails.get(entry.getKey());
                // Update the travel time to the adjacent place
                adjacentPlaceDetails.travelTime = entry.getValue();

                // If the adjacent place is not visited and not in the unvisited set, add it to the queue and set
                if (!adjacentPlaceDetails.isVisited && !unVisitedPlaceSet.contains(adjacentPlaceDetails)) {
                    unVisitedPlaces.offer(adjacentPlaceDetails);
                    unVisitedPlaceSet.add(adjacentPlaceDetails);
                }
            }
        }

        travelPlanPerDayRepository.save(tripPlanDetailsPerDay);
    }

    public void autoPath (List<Place> places, String dateTime, Time startTime, Time endTime){

        Time currentTime = startTime;
        PlaceVisitDetails prevPlaceVisitDetails = null;

        for(Place place : places){

            int arrivalTimeInMinutes = graphBuilder.timeToMinutes(currentTime) + prevPlaceVisitDetails.travelTime;
            int leaveTimeInMinutes = arrivalTimeInMinutes + prevPlaceVisitDetails.stayDuration;
            int travelTime = graphBuilder.adjacentNodes.get(prevPlaceVisitDetails.place).get(place);

            // If the place is not open or if the stay duration exceeds the limit, skip this place
            if (!graphBuilder.travelInOpenTime(graphBuilder, prevPlaceVisitDetails, arrivalTimeInMinutes, dateTime) || leaveTimeInMinutes > graphBuilder.timeToMinutes(endTime)) {
                System.err.println("The trip plan per day is not working because " + place.placeName() + "visit time is not available for open time or close time or day endtime");
            }

            PlaceVisitDetails currentPlaceDetails = graphBuilder.placeDetails.get(place);
            currentPlaceDetails.startTime = graphBuilder.minutesToTime(arrivalTimeInMinutes);
            currentPlaceDetails.endTime = graphBuilder.minutesToTime(leaveTimeInMinutes);
            currentPlaceDetails.travelTime = travelTime;

            visits.add(currentPlaceDetails);
            prevPlaceVisitDetails = currentPlaceDetails;

        }
        travelPlanPerDayRepository.save(tripPlanDetailsPerDay);
    }
}
