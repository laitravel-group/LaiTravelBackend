package com.laitravel.laitravelbe.tripplan;

import com.google.common.reflect.TypeToken;
import com.laitravel.laitravelbe.db.TripRepository;
import com.laitravel.laitravelbe.db.entity.TripPlanEntity;
import com.laitravel.laitravelbe.gson.GsonUtil;
import com.laitravel.laitravelbe.model.Place;
import com.laitravel.laitravelbe.model.PlaceVisitDetails;
import com.laitravel.laitravelbe.model.TripPlan;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;
import com.laitravel.laitravelbe.model.request.TripPlanBuildRequestBody;
import com.laitravel.laitravelbe.model.response.TripPlanBuildResponseBody;
import org.springframework.stereotype.Service;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TravelPlanService {

    private TripPlan tripPlan;
    final TripRepository tripRepository;
    final PlaceGraphBuilderService placeGraphBuilderService;
    final TravelPlanPerDayService travelPlanPerDayService;

    public TravelPlanService(TripRepository tripRepository,
                             PlaceGraphBuilderService placeGraphBuilderService,
                             TravelPlanPerDayService travelPlanPerDayService) {
        this.tripRepository = tripRepository;
        this.placeGraphBuilderService = placeGraphBuilderService;
        this.travelPlanPerDayService = travelPlanPerDayService;
    }

    public List<TripPlan> getTripPlanList(String ownerId, String cityName) {
        List<TripPlanEntity> tripPlanEntities = tripRepository.getTripPlanListNoDetails(ownerId);
        return tripPlanEntities.stream()
                .map(entity -> entity.toTripPlanNoDetails(cityName))
                .collect(Collectors.toList());
    }

    public List<TripPlanDetailsPerDay> getTripPlanDetails(int tripId) {
        TripPlanEntity tripPlanEntity = tripRepository.getTripPlanDetails(tripId);
        if (tripPlanEntity != null) {
            String detailsJson = tripPlanEntity.details();
            Type listType = new TypeToken<ArrayList<TripPlanDetailsPerDay>>() {}.getType();
            return GsonUtil.gson.fromJson(detailsJson, listType);
        } else {
            throw new NoSuchElementException("No trip plan found with the given tripId.");
        }
    }

    public TripPlanBuildResponseBody buildRecommendedTripPlanPerDay(TripPlanBuildRequestBody requestBody) {
        Boolean init = requestBody.init();
        TripPlanDetailsPerDay desiredPlan = requestBody.desiredPlan();

        Place start = desiredPlan.startLocation();
        Date date = desiredPlan.date();
        LocalTime startTime = desiredPlan.startTime();
        LocalTime endTime = desiredPlan.endTime();

        // If init is true, create a new trip plan per day
        if (init) {
            travelPlanPerDayService.buildPlaceVisitDetails(requestBody.desiredPlan().visits());
            travelPlanPerDayService.getDistance(requestBody.desiredPlan().startLocation(), requestBody.desiredPlan().visits());
            TripPlanDetailsPerDay proposedPlan = travelPlanPerDayService.calculateShortestPath(start, date, startTime, endTime);
            addOrUpdateTravelPlan(proposedPlan);
            return new TripPlanBuildResponseBody(true, proposedPlan);
        }
        else {
            return new TripPlanBuildResponseBody(false, desiredPlan);
        }
    }

    public TripPlanBuildResponseBody updateTripPlanPerDay(TripPlanBuildRequestBody requestBody) {
        Boolean init = requestBody.init();
        TripPlanDetailsPerDay desiredPlan = requestBody.desiredPlan();

        Date date = desiredPlan.date();
        LocalTime startTime = desiredPlan.startTime();
        LocalTime endTime = desiredPlan.endTime();
        List<PlaceVisitDetails> visits = desiredPlan.visits();

        // If init is true, create a new trip plan per day
        if (init) {
            travelPlanPerDayService.buildPlaceVisitDetails(requestBody.desiredPlan().visits());
            travelPlanPerDayService.getDistance(requestBody.desiredPlan().startLocation(), requestBody.desiredPlan().visits());
            TripPlanDetailsPerDay proposedPlan = travelPlanPerDayService.autoPath(visits, date, startTime, endTime);
            addOrUpdateTravelPlan(proposedPlan);
            return new TripPlanBuildResponseBody(true, proposedPlan);
        }
        // If init is false, maybe you want to modify the current desired plan based on some algorithm.
        else {
            return new TripPlanBuildResponseBody(false, desiredPlan);
        }
    }

    public void addOrUpdateTravelPlan (TripPlanDetailsPerDay tripPlanDetailsPerDay) {
        List<TripPlanDetailsPerDay> details = tripPlan.details();
        int indexToUpdate = -1;
        for (int i = 0; i < details.size(); i++){
            if (details.get(i).date().equals(tripPlanDetailsPerDay.date())){
                // find same date travel plan then update it
                indexToUpdate = i;
                break;
            }
        }
        if (indexToUpdate != -1) {
            details.set(indexToUpdate, tripPlanDetailsPerDay);
        } else {
            // if there is no same date travel plan then add it
            details.add(tripPlanDetailsPerDay);
        }
    }

    public void deleteTripById(String tripId) {
        tripRepository.deleteById(tripId);
    }

    public void saveTripPlan(String ownerId) {
        tripRepository.save(tripPlan.toTripPlanEntity(ownerId));
    }


}
