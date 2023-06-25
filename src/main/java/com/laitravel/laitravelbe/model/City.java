package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record City(
        @JsonProperty("city_id") String cityId,
        @JsonProperty("city_name") String cityName
) {
}
