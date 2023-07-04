package com.laitravel.laitravelbe.db.entity;

import com.laitravel.laitravelbe.model.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("user")
public record UserEntity(
        @Id
        String userId,
        String displayName,
        String password,
        String avatar
) {
    public User toUser() {
        return new User(userId, displayName, null, avatar);
    }
}
