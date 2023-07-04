package com.laitravel.laitravelbe.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("city")
public record CityEntity(
        @Id
        String cityId,
        String cityName
) {
}