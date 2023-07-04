package com.laitravel.laitravelbe.user;



import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends ListCrudRepository<User, String> {

    @Query("SELECT * FROM users WHERE user_id = :userID")
    User findByUserID(@Param("userID") String userID);

    @Query("SELECT * FROM users WHERE display_name = :displayName")
    List<User> findByDisplayName(@Param("displayName") String displayName);

    @Query("SELECT * FROM users WHERE display_name LIKE %:keyword%")
    List<User> searchByDisplayName(@Param("keyword") String keyword);

    @Query("SELECT * FROM users WHERE avatar IS NULL")
    List<User> findUsersWithoutAvatar();

    // Add more custom query methods as needed
}

