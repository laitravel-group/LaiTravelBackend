package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;

public record TripPlan(
        @JsonProperty("trip_id") String tripId,
        @JsonProperty("destination_city") String destinationCity,
        @JsonProperty("start_datetime") Timestamp startDatetime,
        @JsonProperty("end_datetime") Timestamp endDatetime,
        List<TripPlanDetailsPerDay> details
        ) {
}
