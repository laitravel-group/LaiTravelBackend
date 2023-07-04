package com.laitravel.laitravelbe.db;

import com.laitravel.laitravelbe.db.entity.CityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends CrudRepository<CityEntity, String> {

    CityEntity findByCityId (String cityId);
    List<CityEntity> findByCityName (String cityName);
}
