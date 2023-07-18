package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laitravel.laitravelbe.db.entity.UserEntity;

public record User(
        String username,
        @JsonProperty("display_name") String displayName,
        String password,
        String avatar
) {
    public User(String username, String displayName, String password) {
        this(username, displayName, password, null);
    }

    public User cloneWithoutPassword() {
        return new User(username, displayName, null, avatar);
    }

    public UserEntity toUserEntity() {
        // TODO
        //  : change this to encrypt the password
        return new UserEntity(username, displayName, password, avatar);
    }

}
