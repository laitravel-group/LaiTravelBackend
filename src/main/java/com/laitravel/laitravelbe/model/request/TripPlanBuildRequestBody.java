package com.laitravel.laitravelbe.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;

public record TripPlanBuildRequestBody(
        Boolean init,
        @JsonProperty("desire_plan") TripPlanDetailsPerDay desiredPlan
) {
}
