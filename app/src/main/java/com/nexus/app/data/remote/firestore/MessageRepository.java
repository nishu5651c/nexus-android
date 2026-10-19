package com.nexus.app.data.remote.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nexus.app.data.remote.firestore.models.MessageModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * MessageRepository handles message CRUD and real-time listeners with pagination.
 *
 * Important:
 * - Messages are written under conversations/{conversationId}/messages/{messageId}
 * - Message document contains clientId for deduplication.
 * - createdAt uses server timestamp.
 */
public class MessageRepository {
    private static final String TAG = "MessageRepository";
    private final FirestoreManager manager;

    public MessageRepository() {
        manager = FirestoreManager.getInstance();
    }

    public void sendMessage(@NonNull String conversationId,
                            @NonNull String clientId,
                            @NonNull String senderId,
                            @Nullable String text,
                            @Nullable List<Map<String, Object>> attachments,
                            @Nullable OnCompleteListener callback) {

        String messageId = "msg_" + UUID.randomUUID().toString();
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", messageId);
        payload.put("clientId", clientId);
        payload.put("conversationId", conversationId);
        payload.put("senderId", senderId);
        payload.put("text", text);
        payload.put("attachments", attachments);
        payload.put("edited", false);
        payload.put("deleted", false);
        payload.put("createdAt", manager.serverTimestamp());

        manager.db()
                .collection("conversations")
                .document(conversationId)
                .collection("messages")
                .document(messageId)
                .set(payload)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "sendMessage failed", task.getException());
                    } else {
                        // Update conversation's last message summary atomically
                        Map<String, Object> convoUpdate = new HashMap<>();
                        convoUpdate.put("lastMessageText", text != null ? text : "");
                        convoUpdate.put("lastMessageSenderId", senderId);
                        convoUpdate.put("updatedAt", manager.serverTimestamp());
                        manager.db().collection("conversations").document(conversationId).update(convoUpdate);
                    }
                    if (callback != null) callback.onComplete(task);
                });
    }

    /**
     * Real-time listener for most recent messages (descending). Caller should manage registration.
     */
    public ListenerRegistration addMessagesListener(String conversationId, int pageSize, com.google.firebase.firestore.EventListener<QuerySnapshot> listener) {
        Query q = manager.db()
                .collection("conversations")
                .document(conversationId)
                .collection("messages")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(pageSize);
        return manager.addQueryListener(q, (snapshots, e) -> {
            if (e != null) {
                listener.onEvent(null, e);
                return;
            }
            listener.onEvent(snapshots, null);
        });
    }

    /**
     * Pagination: fetch next page older than lastDocumentSnapshot (pass last doc snapshot)
     */
    public void fetchMessagesPage(String conversationId, int pageSize, @Nullable DocumentSnapshot lastDoc, OnFetchPageCallback callback) {
        Query q = manager.db()
                .collection("conversations")
                .document(conversationId)
                .collection("messages")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(pageSize);

        if (lastDoc != null) {
            q = q.startAfter(lastDoc);
        }

        q.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onSuccess(task.getResult());
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    public interface OnFetchPageCallback {
        void onSuccess(QuerySnapshot result);
        void onFailure(Exception e);
    }
}
