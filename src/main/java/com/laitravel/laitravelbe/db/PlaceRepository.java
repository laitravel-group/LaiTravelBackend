package com.laitravel.laitravelbe.db;

import com.laitravel.laitravelbe.db.entity.PlaceEntity;
import com.laitravel.laitravelbe.model.OpeningHours;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PlaceRepository extends ListCrudRepository<PlaceEntity, String> {

    List<PlaceEntity> findByCityId(String cityId);
    List<PlaceEntity> findByPlaceName(String placeName);
    PlaceEntity findByPlaceId (String placeId);
    //@Modifying
    //@Query("INSERT INTO place (place_id, place_name, city_id, lat, lng, photo, types, formatted_address, description, rating, opening_hours, last_updated) VALUES (:placeId, :placeName, :cityId, :lat, :lng, :photo, :types, :formattedAddress, :description, :rating, :openingHours, :lastUpdated)")
    //void insertPlace(String placeId, String placeName, String cityId, Double lat, Double lng, String photo, List<String> types,
    //                 String formattedAddress, String description, Float rating, List<OpeningHours> openingHours, Timestamp lastUpdated);

    @Modifying
    @Query("INSERT INTO place (place_id, city_id) VALUES (:placeId, :cityId)")
    void insertPlaceId(String placeId, String cityId);
}
