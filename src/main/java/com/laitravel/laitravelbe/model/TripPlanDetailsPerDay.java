package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TripPlanDetailsPerDay(
        String date,
        @JsonProperty("start_location") Place startLocation,
        @JsonProperty("start_time") String startTime,
        @JsonProperty("end_time") String endTime,
        List<PlaceVisitDetails> visits
) {
}
