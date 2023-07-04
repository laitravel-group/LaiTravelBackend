package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.db.entity.PlaceEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public record Place(
        @JsonProperty("place_id") String placeId,

        @JsonProperty("place_name") String placeName,
        @JsonProperty("city_id") String cityId,
        Double lat,
        Double lng,
        String photo,
        List<String> types,
        @JsonProperty("formatted_address") String formattedAddress,
        String description,
        Float rating,
        @JsonProperty("opening_hours")
        List<OpeningHours> openingHours
) {
    public PlaceEntity toPlaceEntity() {
        return new PlaceEntity(placeId, placeName, cityId, lat, lng, photo, types, formattedAddress, description, rating, openingHours, new Timestamp(new Date().getTime()));
    }
}