package com.laitravel.laitravelbe.db;

import com.laitravel.laitravelbe.db.entity.CityEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends ListCrudRepository<CityEntity, String> {
    List<CityEntity> findByCityName (String cityName);
    CityEntity findByCityId (String cityId);
    @Modifying
    @Query("INSERT INTO city (city_id, city_name) VALUES (:cityId, :cityName)")
    void insertCity(String cityId, String cityName);
}
