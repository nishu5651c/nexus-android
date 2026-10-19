package com.nexus.app.data.remote.firestore.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

/**
 * conversations/{conversationId}
 *
 * Fields:
 * - id: string (document id)
 * - title: string (for groups)
 * - isGroup: boolean
 * - ownerId: string (creator)
 * - memberIds: array of strings
 * - workspaceId: optional string
 * - imageUrl: optional string
 * - lastMessage: map / or fields for last message summary
 * - updatedAt: timestamp (server)
 */
public class ConversationModel {
    public String id;
    public String title;
    public boolean isGroup;
    public String ownerId;
    public List<String> memberIds;
    public String workspaceId;
    public String imageUrl;
    public String lastMessageText;
    public String lastMessageSenderId;

    @ServerTimestamp
    public Date updatedAt;

    public ConversationModel() {}

    public ConversationModel(String id, String title, boolean isGroup, String ownerId, List<String> memberIds) {
        this.id = id;
        this.title = title;
        this.isGroup = isGroup;
        this.ownerId = ownerId;
        this.memberIds = memberIds;
    }
}
