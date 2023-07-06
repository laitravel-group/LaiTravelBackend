package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Time;

public record PlaceVisitDetails(
        Place place,
        @JsonProperty("travel_time") Integer travelTime, /* in minutes */
        @JsonProperty("start_time") String startTime,
        @JsonProperty("end_time") String endTime,
        @JsonProperty("stay_duration") Integer stayDuration /* in minutes */
        ) {
}
