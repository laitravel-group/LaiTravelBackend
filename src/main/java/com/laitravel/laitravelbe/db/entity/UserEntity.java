package com.laitravel.laitravelbe.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public record UserEntity(
        @Id
        @Column("UserID")
        @JsonProperty("username")
        String userId,
        @Column("Password")
        String password,
        @JsonProperty("display_name")
        @Column("DisplayName")
        String displayName,
        @Column("Avatar")
        String avatar
) {
}
