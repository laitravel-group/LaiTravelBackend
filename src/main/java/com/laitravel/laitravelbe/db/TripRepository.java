package com.laitravel.laitravelbe.db;

import com.laitravel.laitravelbe.db.entity.TripPlanEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface TripRepository extends ListCrudRepository<TripPlanEntity, Integer> {

    @Query("SELECT trip_id, city_id, start_date, end_date FROM trip_plan WHERE owner_id = :ownerId")
    List<TripPlanEntity> getTripPlanListNoDetails(String ownerId);

    @Query("SELECT details FROM trip_plan WHERE trip_id = :tripId")
    TripPlanEntity getTripPlanDetails(int tripId);

    @Modifying
    @Query("DELETE FROM trip_plan WHERE trip_id = :tripId")
    void deleteById(String tripId);


}
