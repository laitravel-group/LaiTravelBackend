package com.laitravel.laitravelbe.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.model.TripPlan;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("trip")
public record TripEntity(
        @Id
        TripPlan tripPlan,
        TripPlanDetailsPerDay details
) {
}
