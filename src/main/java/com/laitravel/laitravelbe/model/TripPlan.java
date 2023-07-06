package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.db.entity.TripPlanEntity;
import com.laitravel.laitravelbe.util.GsonUtil;
import com.laitravel.laitravelbe.util.DateTimeUtils;

import java.util.List;

public record TripPlan(
        @JsonProperty("trip_id") String tripId,
        @JsonProperty("city_id") String cityId,
        @JsonProperty("destination_city") String destinationCity,
        @JsonProperty("start_date") String startDate,
        @JsonProperty("end_date") String endDate,
        List<TripPlanDetailsPerDay> details
) {
    public TripPlanEntity toTripPlanEntity(String ownerId) {
        return new TripPlanEntity(tripId, ownerId, cityId,
                DateTimeUtils.dateStringToTimestamp(startDate),
                DateTimeUtils.dateStringToTimestamp(endDate),
                GsonUtil.gson.toJson(details));
    }
}
