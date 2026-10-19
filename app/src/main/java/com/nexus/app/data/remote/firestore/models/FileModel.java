package com.nexus.app.data.remote.firestore.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * files/{fileId}
 * metadata about uploaded files.
 */
public class FileModel {
    public String id;
    public String name;
    public long size;
    public String mimeType;
    public String storagePath; // path in Firebase Storage
    public String uploaderId;
    public String conversationId; // optional
    public String workspaceId; // optional
    @ServerTimestamp
    public Date uploadedAt;

    public FileModel() {}
}
