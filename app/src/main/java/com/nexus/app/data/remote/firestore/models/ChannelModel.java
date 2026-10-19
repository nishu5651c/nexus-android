package com.nexus.app.data.remote.firestore.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

/**
 * workspaces/{workspaceId}/channels/{channelId}
 */
public class ChannelModel {
    public String id;
    public String workspaceId;
    public String name;
    public String description;
    public boolean isPrivate;
    public List<String> memberIds;
    public String createdBy;

    @ServerTimestamp
    public Date createdAt;

    public ChannelModel() {}
}
