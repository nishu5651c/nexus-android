package com.nexus.app.data.remote.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Central Firestore helper: provides the Firestore instance, server timestamp helpers,
 * listener management utilities, and a small executor for background tasks.
 */
public class FirestoreManager {
    private static final String TAG = "FirestoreManager";
    private static FirestoreManager instance;
    private final FirebaseFirestore db;
    private final Executor ioExecutor;

    private FirestoreManager() {
        db = FirebaseFirestore.getInstance();
        ioExecutor = Executors.newSingleThreadExecutor();
        // Optional: configure settings if needed
    }

    public static synchronized FirestoreManager getInstance() {
        if (instance == null) instance = new FirestoreManager();
        return instance;
    }

    public FirebaseFirestore db() {
        return db;
    }

    public Executor ioExecutor() {
        return ioExecutor;
    }

    public FieldValue serverTimestamp() {
        return FieldValue.serverTimestamp();
    }

    public ListenerRegistration addQueryListener(Query query, EventListener<QuerySnapshot> listener) {
        try {
            return query.addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    listener.onEvent(null, null);
                    return;
                }
                listener.onEvent(snapshots, null);
            });
        } catch (Exception ex) {
            Log.w(TAG, "addQueryListener failed", ex);
            return null;
        }
    }

    public ListenerRegistration addDocListener(com.google.firebase.firestore.DocumentReference docRef, EventListener<DocumentSnapshot> listener) {
        try {
            return docRef.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    Log.w(TAG, "doc listen:error", e);
                    listener.onEvent(null, null);
                    return;
                }
                listener.onEvent(snapshot, null);
            });
        } catch (Exception ex) {
            Log.w(TAG, "addDocListener failed", ex);
            return null;
        }
    }
}
