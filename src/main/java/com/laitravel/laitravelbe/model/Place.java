package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.db.entity.PlaceEntity;
import com.laitravel.laitravelbe.util.GsonUtil;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
        @JsonProperty("opening_hours") List<OpeningHours> openingHours
) {
    public PlaceEntity toPlaceEntity() {
        return new PlaceEntity(placeId, placeName, cityId, lat, lng, photo, GsonUtil.gson.toJson(types), formattedAddress, description, rating, GsonUtil.gson.toJson(openingHours), new Timestamp(new Date().getTime()));
    }

    public OpeningHours getOpeningHoursForDate(LocalDate date){
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (openingHours == null) {
            return new OpeningHours(dayOfWeek, "09:00","18:00");
        }
        List<OpeningHours> openingHoursForDate = openingHours.stream()
                .filter(dayOpeningHours -> dayOpeningHours.dayOfWeek().equals(dayOfWeek)).toList();
        return openingHoursForDate.isEmpty() ? null : openingHoursForDate.get(0);
    }
}
