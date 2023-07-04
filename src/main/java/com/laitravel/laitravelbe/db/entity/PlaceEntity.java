package com.laitravel.laitravelbe.db.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.model.OpeningHours;
import com.laitravel.laitravelbe.model.Place;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Table("place")
public record PlaceEntity(
        @Id
        String placeId,

        String placeName,

        String cityId,

        Double lat,
        Double lng,
        String photo,
        List<String> types,
        String formattedAddress,
        String description,
        Float rating,
        List<OpeningHours> openingHours,
        Timestamp lastUpdated
) {
    public Place toPlace() {
        return new Place(placeId, placeName, cityId, lat, lng, photo, types, formattedAddress, description, rating, openingHours);
    }
}
