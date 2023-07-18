package com.laitravel.laitravelbe.db.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("city")
public record CityEntity(
        @Id
        String cityId,
        String cityName
) {
}
