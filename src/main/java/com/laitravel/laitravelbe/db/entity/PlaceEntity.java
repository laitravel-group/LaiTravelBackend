package com.laitravel.laitravelbe.db.entity;


import com.google.gson.reflect.TypeToken;
import com.laitravel.laitravelbe.util.GsonUtil;
import com.laitravel.laitravelbe.model.OpeningHours;
import com.laitravel.laitravelbe.model.Place;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
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
        String types,
        String formattedAddress,
        String description,
        Float rating,
        String openingHours,
        Timestamp lastUpdated
) {
    public Place toPlace() {
        return new Place(placeId, placeName, cityId, lat, lng, photo, GsonUtil.gson.fromJson(types, new TypeToken<List<String>>() {}.getType()), formattedAddress, description, rating, GsonUtil.gson.fromJson(openingHours, new TypeToken<List<OpeningHours>>() {}.getType()));
    }
}
