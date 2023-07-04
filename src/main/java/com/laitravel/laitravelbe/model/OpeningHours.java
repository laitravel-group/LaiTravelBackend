package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record OpeningHours(
        @JsonProperty("day_of_week") DayOfWeek dayOfWeek,
        @JsonProperty("open_time") LocalTime openTime,
        @JsonProperty("close_time") LocalTime closeTime

        ) {
}