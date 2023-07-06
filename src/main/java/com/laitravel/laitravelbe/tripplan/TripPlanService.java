package com.laitravel.laitravelbe.tripplan;

import com.google.common.reflect.TypeToken;
import com.laitravel.laitravelbe.db.CityRepository;
import com.laitravel.laitravelbe.db.TripRepository;
import com.laitravel.laitravelbe.db.entity.TripPlanEntity;
import com.laitravel.laitravelbe.model.TripPlan;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;
import com.laitravel.laitravelbe.model.request.TripPlanBuildRequestBody;
import com.laitravel.laitravelbe.model.response.TripPlanBuildResponseBody;
import com.laitravel.laitravelbe.place.PlaceService;
import com.laitravel.laitravelbe.util.DateTimeUtils;
import com.laitravel.laitravelbe.util.GsonUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripPlanService {

    final PlaceService placeService;
    final TripRepository tripRepository;
    final CityRepository cityRepository;

    public TripPlanService(PlaceService placeService, TripRepository tripRepository, CityRepository cityRepository) {
        this.placeService = placeService;
        this.tripRepository = tripRepository;
        this.cityRepository = cityRepository;
    }

    public List<TripPlan> getTripPlanList(String ownerId) {
        List<TripPlanEntity> tripPlanEntities = tripRepository.getTripPlanListNoDetails(ownerId);
        return tripPlanEntities.stream()
                .map(entity ->
                        entity.toTripPlanNoDetails(cityRepository.findByCityId(entity.cityId()).cityName()))
                .toList();
    }

    public List<TripPlanDetailsPerDay> getTripPlanDetails(int tripId) {
        TripPlanEntity tripPlanEntity = tripRepository.getTripPlanDetails(tripId);
        if (tripPlanEntity != null) {
            String detailsJson = tripPlanEntity.details();
            Type listType = new TypeToken<List<TripPlanDetailsPerDay>>() {}.getType();
            return GsonUtil.gson.fromJson(detailsJson, listType);
        } else {
            throw new EmptyResultDataAccessException("No trip plan found with the given tripId.", 1);
        }
    }

    public TripPlanBuildResponseBody buildRecommendedTripPlanPerDay(TripPlanBuildRequestBody requestBody) {
        TripPlanDetailsPerDay desiredPlan = requestBody.desiredPlan();
        if (desiredPlan == null) throw new IllegalArgumentException("Error! Null value is found in request, expected a trip plan.");
        // create a new trip plan per day
        // updated = false because this value is used to identify if the proposed plan and desired plan for
        // a /trip-plan-build-update request are different, this method should not use this value
        TripPlanBuilder tripPlanBuilder = new TripPlanBuilder(
                desiredPlan,
                placeService.getPlaceTravelTimeMap(
                        desiredPlan.startLocation(),
                        desiredPlan.visits().stream().map(details->details.place).collect(Collectors.toCollection(ArrayList::new))));
        TripPlanDetailsPerDay proposedPlan = tripPlanBuilder.calculateShortestPath();
        return new TripPlanBuildResponseBody(false, List.of(proposedPlan));
    }

    public TripPlanBuildResponseBody updateTripPlanPerDay(TripPlanBuildRequestBody requestBody) {
        TripPlanDetailsPerDay desiredPlan = requestBody.desiredPlan();

        LocalTime startTime = DateTimeUtils.timeStringToLocalTime(desiredPlan.startTime());
        LocalTime endTime = DateTimeUtils.timeStringToLocalTime(desiredPlan.endTime());
        // try to connect place of visit in order and validate result
        TripPlanBuilder tripPlanBuilder = new TripPlanBuilder(
                desiredPlan,
                placeService.getPlaceTravelTimeMap(
                        desiredPlan.startLocation(),
                        desiredPlan.visits().stream().map(details->details.place).toList()));
        TripPlanDetailsPerDay proposedPlan = tripPlanBuilder.autoPath();
        // if the result did not change at all, return the same thing
        if (proposedPlan.equals(desiredPlan)) {
            return new TripPlanBuildResponseBody(false, List.of(desiredPlan));
        }
        else {
            return new TripPlanBuildResponseBody(true, List.of(proposedPlan));
        }
    }

    public void deleteTripById(String tripId) {
        tripRepository.deleteById(tripId);
    }

    public void saveTripPlan(String ownerId, @NotNull TripPlan tripPlan) {
        tripRepository.save(tripPlan.toTripPlanEntity(ownerId));
    }


}
