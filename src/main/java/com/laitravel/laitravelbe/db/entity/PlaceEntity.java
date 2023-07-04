package com.laitravel.laitravelbe.db.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.model.OpeningHours;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Table("place")
public record PlaceEntity(
        @Id
        @Column("CityID")
        @JsonProperty("city_id")
        String cityId,
        @Column("PlaceID")
        @JsonProperty("place_id")
        String placeId,
        @Column("NameOfPlace")
        @JsonProperty("place_name")
        String nameOfPlace,
        @Column("LAT")
        double lat,
        @Column("LGT")
        double lgt,
        @Column("Photo")
        String photo,
        @Column("TypeOfPlace")
        List<String> types,
        @Column("FormattedAddress")
        String address,
        @Column("Descriptions")
        String description,
        @Column("OpeningHours")
        @JsonProperty("opening_hours")
        List<OpeningHours> openingHours,
        @Column("UpdateTime")
        LocalDateTime updateTime
) {
}
