package com.nexus.app.data.remote.firestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.nexus.app.data.remote.firestore.models.ConversationModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Repository for conversations: create, list (by membership), update metadata.
 *
 * Security: membership checks enforced by Firestore rules. Client must still send correct data.
 */
public class ConversationRepository {
    private static final String TAG = "ConversationRepo";
    private final FirestoreManager manager;
    private final CollectionReference conversationsRef;

    public ConversationRepository() {
        manager = FirestoreManager.getInstance();
        conversationsRef = manager.db().collection("conversations");
    }

    public void createConversation(@NonNull String title,
                                   boolean isGroup,
                                   @NonNull String ownerId,
                                   @NonNull List<String> memberIds,
                                   OnCompleteListener<DocumentReference> callback) {
        String id = "conv_" + UUID.randomUUID().toString();
        ConversationModel model = new ConversationModel(id, title, isGroup, ownerId, memberIds);
        model.updatedAt = null; // server timestamp will be set on write
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("title", title);
        data.put("isGroup", isGroup);
        data.put("ownerId", ownerId);
        data.put("memberIds", memberIds);
        data.put("updatedAt", manager.serverTimestamp());

        conversationsRef.document(id).set(data).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) Log.w(TAG, "createConversation failed", task.getException());
            if (callback != null) {
                // pass a fake DocumentReference result via Task API? We'll simply call the callback with a completed task.
                // Build a simple Task<DocumentReference> is nontrivial; instead, call the provided OnCompleteListener with null.
                callback.onComplete(task);
            }
        });
    }

    public Query conversationsForUser(String uid) {
        // Query where memberIds array-contains uid
        return conversationsRef.whereArrayContains("memberIds", uid).orderBy("updatedAt", Query.Direction.DESCENDING);
    }

    public DocumentReference conversationRef(String conversationId) {
        return conversationsRef.document(conversationId);
    }
}
