package com.laitravel.laitravelbe.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;

import java.util.List;

public record TripPlanBuildResponseBody(
        @JsonProperty("proposed_plans") List<TripPlanDetailsPerDay> proposedPlans
) {
}
