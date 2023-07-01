package com.laitravel.laitravelbe.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public class User {
    @Id
    @Column("user_id")
    private String userID;

    @Column("display_name")
    private String displayName;

    private String password;
    private String avatar;

    // Constructors, getters, and setters

    public User(String userID, String displayName, String password, String avatar) {
        this.userID = userID;
        this.displayName = displayName;
        this.password = password;
        this.avatar = avatar;
    }

    // Getters and setters

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
