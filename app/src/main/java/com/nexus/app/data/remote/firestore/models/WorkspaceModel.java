package com.nexus.app.data.remote.firestore.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * workspaces/{workspaceId}
 *
 * Fields:
 * - id
 * - name
 * - description
 * - ownerId
 * - imageUrl
 * - createdAt
 */
public class WorkspaceModel {
    public String id;
    public String name;
    public String description;
    public String ownerId;
    public String imageUrl;

    @ServerTimestamp
    public Date createdAt;

    public WorkspaceModel() {}

    public WorkspaceModel(String id, String name, String description, String ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }
}
