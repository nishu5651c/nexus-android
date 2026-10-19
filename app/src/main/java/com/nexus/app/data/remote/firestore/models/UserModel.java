package com.nexus.app.data.remote.firestore.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.Map;

/**
 * users/{uid}
 */
public class UserModel {
    public String uid;
    public String displayName;
    public String email;
    public String photoUrl;
    public Map<String, Object> presence; // optional: {online: boolean, lastSeen: Timestamp}

    @ServerTimestamp
    public Date updatedAt;

    public UserModel() {}

    public UserModel(String uid, String displayName, String email, String photoUrl) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
    }
}
