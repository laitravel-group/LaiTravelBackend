package com.laitravel.laitravelbe.model.response;

import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;

import java.util.List;

public record TripPlanDetailsResponseBody(
        List<TripPlanDetailsPerDay> details
) {
}
