package com.laitravel.laitravelbe.db;


import com.laitravel.laitravelbe.db.entity.UserEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends ListCrudRepository<UserEntity, String> {

    List<UserEntity> findByDisplayName(String displayName);

    UserEntity findByUserId(String userId);


    @Modifying
    @Query("UPDATE user SET display_name = :displayName, avatar = :avatar WHERE user_id = :userId")
    void updateNameByUserId(String userId, String displayName, String avatar);
}
