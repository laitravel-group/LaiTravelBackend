package com.laitravel.laitravelbe.db.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.laitravel.laitravelbe.gson.GsonUtil;
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
        String details
) {
    public TripPlan toTripPlan(String cityName) {
        return new TripPlan(tripId, cityId, cityName, startDate, endDate, GsonUtil.gson.fromJson(details, new TypeToken<List<String>>() {}.getType()));
    }

    public TripPlan toTripPlanNoDetails(String cityName) {
        return new TripPlan(tripId, cityId, cityName, startDate, endDate, null);
    }
}
