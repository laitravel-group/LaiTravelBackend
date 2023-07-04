package com.laitravel.laitravelbe.db;


import com.laitravel.laitravelbe.db.entity.UserEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {

    List<UserEntity> findByDisplayName(String displayName);

    UserEntity findByUserId(String userId);

    @Modifying
    @Query("UPDATE users SET displayName = :displayName, avatar = :avatar WHERE userId = :userId")
    void updateNameByUserId(String userId, String displayName, String avatar);
}
