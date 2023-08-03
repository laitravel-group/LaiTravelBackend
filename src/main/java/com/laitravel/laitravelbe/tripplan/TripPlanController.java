package com.laitravel.laitravelbe.tripplan;
import com.laitravel.laitravelbe.auth.AuthenticationService;
import com.laitravel.laitravelbe.model.TripPlan;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;
import com.laitravel.laitravelbe.model.request.TripPlanBuildRequestBody;
import com.laitravel.laitravelbe.model.response.TripPlanBuildResponseBody;
import com.laitravel.laitravelbe.model.response.TripPlanDetailsResponseBody;
import com.laitravel.laitravelbe.model.response.TripPlanListResponseBody;
import com.laitravel.laitravelbe.model.response.TripPlanUpdateResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class TripPlanController {
    final TripPlanService travelPlanService;
    final AuthenticationService authenticationService;

    public TripPlanController(TripPlanService travelPlanService, AuthenticationService authenticationService) {
        this.travelPlanService = travelPlanService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/trip-plans")
    public TripPlanListResponseBody getTripPlanList(@RequestParam(value="ownerId") String ownerId) {
        List<TripPlan> tripPlans = travelPlanService.getTripPlanList(ownerId);
        return new TripPlanListResponseBody(tripPlans);
    }

    @GetMapping("/trip-plan-details")
    public TripPlanDetailsResponseBody getTripPlanDetails(@RequestParam(value="tripId") Integer tripId) {
        List<TripPlanDetailsPerDay> details = travelPlanService.getTripPlanDetails(tripId);
        return new TripPlanDetailsResponseBody(details);
    }

    @DeleteMapping("/trip-plan-delete")
    public void deleteTripPlan(@RequestParam(value="tripId") String tripId) {
        // delete the trip plan with tripId in DB
        travelPlanService.deleteTripById(tripId);
    }

    @PostMapping("/trip-plan-build")
    public TripPlanBuildResponseBody buildRecommendedTripPlan(@RequestBody TripPlanBuildRequestBody requestBody) {
        // build a TripPlanBuildResponseBody with TripPlanDetailsPerDay
        return new TripPlanBuildResponseBody(travelPlanService.buildRecommendedTripPlanPerDay(requestBody.desiredPlan()));
    }

    @PostMapping("/trip-plan-build-update")
    public TripPlanUpdateResponseBody validateAndUpdateTripPlan(@RequestBody TripPlanBuildRequestBody requestBody) {
        // TODO
        // check if the input trip plan is valid and update it accordingly
        TripPlanDetailsPerDay updatedPlan = travelPlanService.updateTripPlanPerDay(requestBody.desiredPlan());
        if (updatedPlan == null) {
            return new TripPlanUpdateResponseBody(false, requestBody.desiredPlan());
        } else {
            return new TripPlanUpdateResponseBody(true, updatedPlan);
        }
    }

    @PostMapping("/trip-plan-save")
    public void saveTripPlan(HttpServletRequest request, @RequestBody TripPlan tripPlan) {
        final String authHeader = request.getHeader("Authorization");
        final String token = authenticationService.extractToken(authHeader);
        final String ownerId = authenticationService.extractUsernameFromToken(token);
        if (token != null) {
            travelPlanService.saveTripPlan(ownerId, tripPlan);
        }
    }


}
