package com.laitravel.laitravelbe.db.entity;

import com.google.gson.reflect.TypeToken;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;
import com.laitravel.laitravelbe.util.DateTimeUtils;
import com.laitravel.laitravelbe.util.GsonUtil;
import com.laitravel.laitravelbe.model.TripPlan;
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
        return new TripPlan(tripId, cityId, cityName,
                DateTimeUtils.dateTimestampToString(startDate),
                DateTimeUtils.dateTimestampToString(endDate),
                GsonUtil.gson.fromJson(details, new TypeToken<List<TripPlanDetailsPerDay>>() {}.getType()));
    }

    public TripPlan toTripPlanNoDetails(String cityName) {
        return new TripPlan(tripId, cityId, cityName,
                DateTimeUtils.dateTimestampToString(startDate),
                DateTimeUtils.dateTimestampToString(endDate), null);
    }
}
