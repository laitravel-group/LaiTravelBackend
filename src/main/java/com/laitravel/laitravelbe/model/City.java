package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.db.entity.CityEntity;

public record City(
        @JsonProperty("city_id") String cityId,
        @JsonProperty("city_name") String cityName
) {
    public CityEntity toCityEntity() {
        return new CityEntity(cityId, cityName);
    }
}
