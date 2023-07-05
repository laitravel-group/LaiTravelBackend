package com.laitravel.laitravelbe.db.entity;

import com.laitravel.laitravelbe.model.City;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("city")
public record CityEntity(
        @Id
        String cityId,
        String cityName
) {
    public City toCity() {
        return new City(cityId, cityName);
    }

}
