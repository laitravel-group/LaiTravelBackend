package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.laitravel.laitravelbe.db.entity.TripPlanEntity;
import com.laitravel.laitravelbe.gson.GsonUtil;

import java.sql.Timestamp;
import java.util.List;

public record TripPlan(
        @JsonProperty("trip_id") String tripId,
        @JsonProperty("city_id") String cityId,
        @JsonProperty("destination_city") String destinationCity,
        @JsonProperty("start_datetime") Timestamp startDate,
        @JsonProperty("end_datetime") Timestamp endDate,
        List<TripPlanDetailsPerDay> details
) {
    public TripPlanEntity toTripPlanEntity(String ownerId) {
        return new TripPlanEntity(tripId, ownerId, cityId, startDate, endDate, GsonUtil.gson.toJson(details));
    }
}
