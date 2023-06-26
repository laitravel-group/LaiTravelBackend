package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Place(
        @JsonProperty("place_id") String placeId,
        @JsonProperty("place_name") String placeName,
        Double lat,
        Double lgt,
        String photo,
        List<String> types,
        String address,
        String description,
        @JsonProperty("opening_hours") List<OpeningHours> openingHours,
        @JsonProperty("rating") float rating
) {
}
