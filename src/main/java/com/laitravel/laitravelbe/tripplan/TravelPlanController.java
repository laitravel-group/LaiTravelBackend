package com.laitravel.laitravelbe.tripplan;
import com.laitravel.laitravelbe.model.TripPlan;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;
import com.laitravel.laitravelbe.model.request.TripPlanBuildRequestBody;
import com.laitravel.laitravelbe.model.response.TripPlanBuildResponseBody;
import com.laitravel.laitravelbe.model.response.TripPlanDetailsResponseBody;
import com.laitravel.laitravelbe.model.response.TripPlanListResponseBody;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class TravelPlanController {
    final TravelPlanService travelPlanService;


    public TravelPlanController(TravelPlanService travelPlanService) {
        this.travelPlanService = travelPlanService;
    }

    @GetMapping("/trip-plans")
    public TripPlanListResponseBody getTripPlanList(@RequestParam(value="ownerId") String ownerId,  @RequestParam(value="cityName") String cityName) {
        // TODO
        // get list from DB
        List<TripPlan> tripPlans = travelPlanService.getTripPlanList(ownerId, cityName);
        return new TripPlanListResponseBody(tripPlans);
    }

    @GetMapping("/trip-plan-details")
    public TripPlanDetailsResponseBody getTripPlanDetails(@RequestParam(value="tripId") String tripId) {
        // TODO
        // get details from DB given tripId
        int tripIdInt = Integer.parseInt(tripId);
        List<TripPlanDetailsPerDay> details = travelPlanService.getTripPlanDetails(tripIdInt);
        return new TripPlanDetailsResponseBody(details);
    }

    @PostMapping("/trip-plan-build")
    public TripPlanBuildResponseBody buildRecommendedTripPlan(@RequestBody TripPlanBuildRequestBody requestBody) {
        // TODO
        // build a TripPlanBuildResponseBody with TripPlanDetailsPerDay
        return travelPlanService.buildRecommendedTripPlanPerDay(requestBody);
    }

    @PostMapping("/trip-plan-build-update")
    public TripPlanBuildResponseBody validateAndUpdateTripPlan(@RequestBody TripPlanBuildRequestBody requestBody) {
        // TODO
        // check if the input trip plan is valid and update it accordingly
        return travelPlanService.updateTripPlanPerDay(requestBody);
    }

    @DeleteMapping("/trip-plan-delete")
    public void deleteTripPlan(@RequestParam(value="tripId") String tripId) {
        // TODO
        // delete the trip plan with tripId in DB
        travelPlanService.deleteTripById(tripId);
    }

    @PostMapping("/trip-plan-save")
    public void saveTripPlan(@RequestParam(value="ownerId") String ownerId) {

        travelPlanService.saveTripPlan(ownerId);
    }


}
