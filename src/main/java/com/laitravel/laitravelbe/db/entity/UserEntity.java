package com.laitravel.laitravelbe.db.entity;

import com.laitravel.laitravelbe.model.User;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("users")
public record UserEntity(
        @Id
        String username,
        String displayName,
        String password,
        String avatar
) {
    public User toUser() {
        return new User(username, displayName, null, avatar);
    }
}
