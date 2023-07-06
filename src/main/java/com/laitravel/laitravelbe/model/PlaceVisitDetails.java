package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Time;
import java.time.LocalTime;

public class PlaceVisitDetails {
        public final Place place;
        public Integer travelTime;
        public LocalTime startTime;
        public LocalTime endTime;
        public final Integer stayDuration;
        public boolean isVisited;

        public PlaceVisitDetails(
                @JsonProperty("place") Place place,
                @JsonProperty("travel_time") Integer  travelTime,
                @JsonProperty("start_time") LocalTime startTime,
                @JsonProperty("end_time") LocalTime endTime,
                @JsonProperty("stay_duration") Integer stayDuration
        ) {
                this.place = place;
                this.travelTime = travelTime;
                this.startTime = startTime;
                this.endTime = endTime;
                this.stayDuration = stayDuration;
        }

}
