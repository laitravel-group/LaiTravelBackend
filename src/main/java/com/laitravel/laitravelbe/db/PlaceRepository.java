package com.laitravel.laitravelbe.db;

import com.laitravel.laitravelbe.db.entity.PlaceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends CrudRepository<PlaceEntity, String> {

    List<PlaceEntity> findByCityId(String cityId);
    List<PlaceEntity> findByNameOfPlace(String nameOfPlace);
    PlaceEntity findByPlaceId (String placeId);

    //根据关键字在场所名称和地址中进行模糊查询，返回匹配的场所信息列表。

}
