package com.laitravel.laitravelbe.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.model.TripPlan;

import java.util.List;

public record TripPlanListResponseBody(
        @JsonProperty("trip_plans") List<TripPlan> tripPlans
) {
}
