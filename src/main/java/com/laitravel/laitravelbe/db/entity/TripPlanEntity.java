package com.laitravel.laitravelbe.db.entity;

import com.laitravel.laitravelbe.model.TripPlan;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.List;

@Table("trip_plan")
public record TripPlanEntity(
        @Id
        String tripId,
        String ownerId,
        String cityId,
        Timestamp startDate,
        Timestamp endDate,
        List<TripPlanDetailsPerDay> details
) {
    public TripPlan toTripPlan(String cityName) {
        return new TripPlan(tripId, cityId, cityName, startDate, endDate, details);
    }

    public TripPlan toTripPlanNoDetails(String cityName) {
        return new TripPlan(tripId, cityId, cityName, startDate, endDate, null);
    }
}
