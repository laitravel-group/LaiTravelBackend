package com.laitravel.laitravelbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
        return new User(username(), displayName(), null, avatar());
    }


}
