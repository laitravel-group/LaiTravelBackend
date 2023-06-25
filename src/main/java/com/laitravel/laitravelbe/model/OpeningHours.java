package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.sql.Time;
import java.time.DayOfWeek;

public record OpeningHours(
        @JsonProperty("day_of_week") DayOfWeek dayOfWeek,
        @JsonProperty("open_time") Time openTime,
        @JsonProperty("close_time") Time closeTime

        ) {
}
