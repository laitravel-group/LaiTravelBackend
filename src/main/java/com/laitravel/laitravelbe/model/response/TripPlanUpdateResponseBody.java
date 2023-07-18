package com.laitravel.laitravelbe.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;

public record TripPlanUpdateResponseBody(
        Boolean updated,
        @JsonProperty("proposed_plan") TripPlanDetailsPerDay proposedPlan
) {
}
