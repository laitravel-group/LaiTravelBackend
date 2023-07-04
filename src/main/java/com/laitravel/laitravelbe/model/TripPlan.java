package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.db.entity.TripPlanEntity;

import java.sql.Timestamp;
import java.util.List;

public record TripPlan(
        @JsonProperty("trip_id") String tripId,
        @JsonProperty("destination_city") String destinationCity,
        @JsonProperty("start_datetime") Timestamp startDate,
        @JsonProperty("end_datetime") Timestamp endDate,
        List<TripPlanDetailsPerDay> details
) {
    public TripPlanEntity toTripPlanEntity(String ownerId, String cityId) {
        return new TripPlanEntity(tripId, ownerId, cityId, startDate, endDate, details);
    }
}
