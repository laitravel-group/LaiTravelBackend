package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.db.entity.PlaceEntity;
import com.laitravel.laitravelbe.util.GsonUtil;

import java.sql.Timestamp;
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
        return new PlaceEntity(placeId, placeName, cityId, lat, lng, photo, GsonUtil.gson.toJson(types), formattedAddress, description, rating, GsonUtil.gson.toJson(openingHours), new Timestamp(new Date().getTime()));
    }
}
