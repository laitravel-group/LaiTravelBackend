package com.laitravel.laitravelbe.db;

import com.laitravel.laitravelbe.db.entity.PlaceEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends ListCrudRepository<PlaceEntity, String> {

    List<PlaceEntity> findByCityId(String cityId);
    List<PlaceEntity> findByPlaceName(String placeName);
    PlaceEntity findByPlaceId (String placeId);

    @Modifying
    @Query("INSERT INTO place (place_id, city_id) VALUES (:placeId, :cityId)")
    void insertPlaceId(String placeId, String cityId);
}
