package com.laitravel.laitravelbe.db;

import com.laitravel.laitravelbe.db.entity.TripPlanEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface TripRepository extends CrudRepository<TripPlanEntity, Integer> {

    //List<TripEntity> findByOwnerId(String ownerId);

    //和黄zhe沟通一下
    @Modifying
    @Query("UPDATE trip SET cityId = :cityId, startDate = startDate, endDate = :endDate WHERE tripId = :tripId")
    void updateCityById(int tripId, String cityId, LocalDateTime startDate, LocalDateTime endDate);

    @Modifying
    @Query("DELETE FROM trip WHERE ownerId = :ownerId")
    void deleteById(String ownerId);
}
