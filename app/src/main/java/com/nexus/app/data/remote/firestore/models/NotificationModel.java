package com.nexus.app.data.remote.firestore.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * notifications/{notificationId}
 */
public class NotificationModel {
    public String id;
    public String userId; // recipient
    public String type; // message, mention, invitation, file, etc.
    public String title;
    public String body;
    public String targetId; // e.g., conversationId or workspaceId
    public boolean read;

    @ServerTimestamp
    public Date createdAt;

    public NotificationModel() {}
}
