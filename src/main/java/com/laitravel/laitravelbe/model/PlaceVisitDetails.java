package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.tripplan.TripPlanUtils;
import com.laitravel.laitravelbe.util.DateTimeUtils;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class PlaceVisitDetails {
        public final Place place;
        @JsonProperty("travel_time")
        public Integer travelTime;
        @JsonProperty("start_time")
        public String startTime;
        @JsonProperty("end_time")
        public String endTime;
        @JsonProperty("stay_duration")
        public final Integer stayDuration;  // in minutes
        @JsonIgnore
        public boolean isVisited;

        public PlaceVisitDetails(
                Place place,
                Integer  travelTime,
                String startTime,
                String endTime,
                Integer stayDuration
        ) {
                this.place = place;
                this.travelTime = travelTime;
                this.startTime = startTime;
                this.endTime = endTime;
                this.stayDuration = stayDuration;
                this.isVisited = false;
        }

        // return true if start time is equal or after open time
        // and end time is equal or before close time
        public boolean canVisitAtDateAndTime(LocalDate date, LocalTime startTime) {
                OpeningHours openingHoursForDate = place.getOpeningHoursForDate(date);
                if (openingHoursForDate == null) return false;
                LocalTime openTime = DateTimeUtils.timeStringToLocalTime(openingHoursForDate.openTime());
                LocalTime closeTime = DateTimeUtils.timeStringToLocalTime(openingHoursForDate.closeTime());
                LocalTime endTime = startTime.plusMinutes(stayDuration);
                return !startTime.isBefore(openTime) &&
                        !endTime.isAfter(closeTime);
        }

}
