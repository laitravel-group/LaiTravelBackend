package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

public record TripPlanDetailsPerDay(
        Date date,
        @JsonProperty("start_location") Place startLocation,
        @JsonProperty("date_Time") String dateTime,
        @JsonProperty("start_time") LocalTime startTime,
        @JsonProperty("end_time") LocalTime endTime,

        List<PlaceVisitDetails> visits
) {
}
