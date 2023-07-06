package com.laitravel.laitravelbe;

import com.laitravel.laitravelbe.model.Place;
import com.laitravel.laitravelbe.model.PlaceVisitDetails;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;
import com.laitravel.laitravelbe.model.request.TripPlanBuildRequestBody;
import com.laitravel.laitravelbe.model.response.TripPlanBuildResponseBody;
import com.laitravel.laitravelbe.place.PlaceService;
import com.laitravel.laitravelbe.tripplan.TravelPlanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@RestController
public class TestController {
    final TravelPlanService travelPlanService;
    final PlaceService placeService;
    public TestController(TravelPlanService travelPlanService,PlaceService placeService) {
        this.travelPlanService = travelPlanService;
        this.placeService = placeService;
    }
    @GetMapping("/test")
    public TripPlanBuildResponseBody test() {
        Place startPlace = new Place(
                "start_id",
                "start",
                "city_id",
                -33.88275286637396,
                151.20672917659843,
                "start_photo",
                Arrays.asList("type1", "type2"),
                "start_address",
                "start_description",
                4.5f,
                null
        );
        Place bondiBeach = new Place(
                "bondi_beach_id",
                "Bondi Beach",
                "city_id",
                -33.89119321916367,
                151.2773225059465,
                "bondi_beach_photo",
                Arrays.asList("type1", "type2"),
                "bondi_beach_address",
                "bondi_beach_description",
                4.8f,
                null
        );
        Place hydePark = new Place(
                "hyde_park_id",
                "Hyde Park",
                "city_id",
                -33.87344060253566,
                151.21131704281586,
                "hyde_park_photo",
                Collections.singletonList("park"),
                "hyde_park_address",
                "hyde_park_description",
                4.6f,
                null
        );

        Place operaHouse = new Place(
                "opera_house_id",
                "Opera House",
                "city_id",
                -33.85669344935661,
                151.21510537950104,
                "opera_house_photo",
                Collections.singletonList("landmark"),
                "opera_house_address",
                "opera_house_description",
                4.9f,
                null
        );
        PlaceVisitDetails startPlaceVisit = new PlaceVisitDetails(startPlace, null, null, null, 60);
        PlaceVisitDetails bondiBeachVisit = new PlaceVisitDetails(bondiBeach, null, null, null, 120);
        PlaceVisitDetails hydeParkVisit = new PlaceVisitDetails(hydePark, null, null, null, 30);
        PlaceVisitDetails operaHouseVisit = new PlaceVisitDetails(operaHouse, null, null, null, 120);


        java.time.LocalDate localDate = java.time.LocalDate.of(2023, 8, 1);
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
        // Create the TripPlanDetailsPerDay
        TripPlanDetailsPerDay planDetails = new TripPlanDetailsPerDay(
                sqlDate,
                startPlace,
                LocalTime.of(9, 0),
                LocalTime.of(20, 0),
                Arrays.asList(startPlaceVisit,bondiBeachVisit, hydeParkVisit, operaHouseVisit)
        );
        // Create the TripPlanBuildRequestBody
        TripPlanBuildRequestBody requestBody = new TripPlanBuildRequestBody(true, planDetails);
        Place origin = startPlace;
        List<Place> destinations = new ArrayList<>();
        destinations.add(bondiBeach);
        destinations.add(hydePark);
        destinations.add(operaHouse);
        //Map<Place, Map<Place,Integer>>   result = placeService.getDistances(origin, destinations);


        TripPlanBuildResponseBody  result = travelPlanService.buildRecommendedTripPlanPerDay(requestBody);

        return result;

    }
}
