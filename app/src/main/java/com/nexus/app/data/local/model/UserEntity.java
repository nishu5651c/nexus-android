package com.nexus.app.data.local.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    public String uid;

    public String displayName;
    public String email;
    public String photoUrl;
    public long lastSeen; // epoch millis

    public UserEntity(String uid, String displayName, String email, String photoUrl, long lastSeen) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.lastSeen = lastSeen;
    }
}
