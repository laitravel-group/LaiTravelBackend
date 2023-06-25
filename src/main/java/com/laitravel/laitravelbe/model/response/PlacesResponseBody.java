package com.laitravel.laitravelbe.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.model.Place;

import java.util.List;

public record PlacesResponseBody(
        @JsonProperty("city_id") String cityId,
        List<Place> places
) {
}
