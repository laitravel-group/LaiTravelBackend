package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public record TripPlanDetailsPerDay(
        Date date,
        @JsonProperty("start_location") Place startLocation,
        @JsonProperty("start_time") Time startTime,
        List<PlaceVisitDetails> visits
) {
}
