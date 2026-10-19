package com.nexus.app.data.remote.firestore.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * conversations/{conversationId}/messages/{messageId}
 *
 * Important fields:
 * - id: string (message id)
 * - senderId: string
 * - text: string (nullable)
 * - attachments: list of maps {fileId, name, size, mime, storagePath}
 * - replyTo: optional messageId
 * - reactions: map of emoji -> array of userIds OR map userId->emoji (choose simple)
 * - edited: boolean
 * - deleted: boolean
 * - createdAt: server timestamp
 * - clientId: optional client-generated uuid for deduplication
 * - status: optional client-side only (not persisted here) — statuses managed locally/repo
 */
public class MessageModel {
    public String id;
    public String clientId;
    public String conversationId;
    public String senderId;
    public String text;
    public List<Map<String, Object>> attachments;
    public String replyTo;
    public Map<String, List<String>> reactions;
    public boolean edited;
    public boolean deleted;

    @ServerTimestamp
    public Date createdAt;

    public MessageModel() {}

    public MessageModel(String id, String clientId, String conversationId, String senderId, String text) {
        this.id = id;
        this.clientId = clientId;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.text = text;
    }
}
