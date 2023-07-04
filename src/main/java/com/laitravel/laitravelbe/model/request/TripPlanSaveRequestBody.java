package com.laitravel.laitravelbe.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;

import java.sql.Timestamp;
import java.util.List;

public record TripPlanSaveRequestBody(
        @JsonProperty("city_id") String cityId,
        @JsonProperty("start_datetime") Timestamp startDatetime,
        @JsonProperty("end_datetime") Timestamp endDatetime,
        List<TripPlanDetailsPerDay> details

) {
}
