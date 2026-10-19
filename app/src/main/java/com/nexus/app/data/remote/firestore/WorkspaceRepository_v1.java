package com.nexus.app.data.remote.firestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.nexus.app.data.remote.firestore.models.WorkspaceModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * WorkspaceRepository: create workspace and manage basic metadata.
 * Membership will be stored in workspaces/{workspaceId}/members/{uid} documents (simple role map).
 */
public class WorkspaceRepository {
    private static final String TAG = "WorkspaceRepo";
    private final FirestoreManager manager;
    private final CollectionReference workspacesRef;

    public WorkspaceRepository() {
        manager = FirestoreManager.getInstance();
        workspacesRef = manager.db().collection("workspaces");
    }

    public void createWorkspace(@NonNull String name, @NonNull String description, @NonNull String ownerId, OnCompleteListener callback) {
        String id = "ws_" + UUID.randomUUID().toString();
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("name", name);
        payload.put("description", description);
        payload.put("ownerId", ownerId);
        payload.put("imageUrl", null);
        payload.put("createdAt", manager.serverTimestamp());

        workspacesRef.document(id).set(payload).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // create owner membership
                Map<String, Object> member = new HashMap<>();
                member.put("role", "OWNER");
                manager.db().collection("workspaces").document(id).collection("members")
                        .document(ownerId).set(member);
            } else {
                Log.w(TAG, "createWorkspace failed", task.getException());
            }
            if (callback != null) callback.onComplete(task);
        });
    }
}
