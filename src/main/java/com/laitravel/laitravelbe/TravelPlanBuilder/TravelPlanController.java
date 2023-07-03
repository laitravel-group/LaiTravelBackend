package com.laitravel.laitravelbe.TravelPlanBuilder;

import com.google.maps.model.DistanceMatrix;
import com.laitravel.laitravelbe.model.Place;
import com.laitravel.laitravelbe.model.TripPlan;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;
import com.laitravel.laitravelbe.placeandinfomanagement.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/travel-plan")
public class TravelPlanController {
    private final TravelPlanPerDayService travelPlanPerDayService;
    private final TravelPlanService travelPlanService;
    private final PlaceService placeService;
    private final PlaceGraphBuilderService placeGraphBuilderService;

    @Autowired
    public TravelPlanController(TravelPlanPerDayService travelPlanPerDayService,TravelPlanService travelPlanService, PlaceGraphBuilderService placeGraphBuilderService, PlaceService placeService) {
        this.travelPlanPerDayService = travelPlanPerDayService;
        this.travelPlanService = travelPlanService;
        this.placeGraphBuilderService = placeGraphBuilderService;
        this.placeService = placeService;
    }

    @PostMapping("/place")
    public ResponseEntity<DistanceMatrix> getDistance(@RequestParam(value="origin") String[] origin, @RequestParam(value="destinations") String[] destinations){
        try {
            DistanceMatrix distanceMatrix = placeService.getDistance(origin, destinations);
            return ResponseEntity.ok(distanceMatrix);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/build")
    public ResponseEntity<String> buildPlaceVisitDetails(@RequestParam(value="place") Place place, @RequestParam(value="stayDuration") Integer stayDuration) {
        try {
            placeGraphBuilderService.buildPlaceVisitDetails(place, stayDuration);
            return ResponseEntity.status(HttpStatus.CREATED).body("PlaceVisitDetails built successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in building PlaceVisitDetails");
        }
    }

    @PostMapping("/shortestPath")
    public ResponseEntity<TripPlanDetailsPerDay> calculateShortestPath(@RequestParam(value="place") Place start, @RequestParam(value="dateTime")String dateTime, @RequestParam(value="startTime") Time startTime, @RequestParam(value="endTime") Time endTime){
        try {
            travelPlanPerDayService.calculateShortestPath(start,dateTime,startTime,endTime);
            return ResponseEntity.ok(travelPlanPerDayService.tripPlanDetailsPerDay);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/autoPath")
    public ResponseEntity<TripPlanDetailsPerDay> autoPath(@RequestParam(value="places") List<Place> places, @RequestParam(value="dateTime")String dateTime, @RequestParam(value="startTime") Time startTime, @RequestParam(value="endTime") Time endTime) {
        try {
            travelPlanPerDayService.autoPath(places,dateTime,startTime,endTime);
            return ResponseEntity.ok(travelPlanPerDayService.tripPlanDetailsPerDay);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/addPlan")
    public ResponseEntity<TripPlan> addTravelPlan (TripPlanDetailsPerDay tripPlanDetailsPerDay) {
        try {
            travelPlanService.addTravelPlan(tripPlanDetailsPerDay);
            return ResponseEntity.ok(travelPlanService.tripPlan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
