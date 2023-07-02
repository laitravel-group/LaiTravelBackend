package com.example.laitravel0626.TravelPlanBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/travel-plan")
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    @Autowired
    public TravelPlanController(TravelPlanService travelPlanService) {
        this.travelPlanService = travelPlanService;
    }

    @PostMapping
    public String calculateShortestPath(@RequestBody ShortestPathRequest request) {
        travelPlanService.calculateShortestPath(request.getStart(), request.getDateTime(), request.getStartTime(), request.getEndTime());
        return "Shortest path calculation completed";
    }

    @PostMapping
    public String autoPath(@RequestBody autoPathRequest request) {
        travelPlanService.autoPath(request.getPlaces(), request.getDateTime(), request.getStartTime(), request.getEndTime());
        return "Shortest path calculation completed";
    }
}
