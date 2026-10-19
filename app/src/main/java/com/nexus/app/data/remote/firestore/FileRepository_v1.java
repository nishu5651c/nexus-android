package com.nexus.app.data.remote.firestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * FileRepository provides helpers to upload files to Firebase Storage and write metadata to Firestore.
 * Note: enforce size/type limits on client and via Storage Rules.
 */
public class FileRepository {
    private static final String TAG = "FileRepository";
    private final com.google.firebase.storage.FirebaseStorage storage;
    private final FirestoreManager manager;

    public FileRepository() {
        storage = com.google.firebase.storage.FirebaseStorage.getInstance();
        manager = FirestoreManager.getInstance();
    }

    public com.google.firebase.storage.StorageReference uploadFile(String uploaderId, android.net.Uri fileUri, String fileName, com.google.firebase.storage.UploadTask.OnProgressListener progressListener) {
        String path = "files/" + uploaderId + "/" + java.util.UUID.randomUUID().toString() + "_" + fileName;
        com.google.firebase.storage.StorageReference ref = storage.getReference().child(path);
        com.google.firebase.storage.UploadTask task = ref.putFile(fileUri);
        if (progressListener != null) task.addOnProgressListener(progressListener);
        task.addOnFailureListener(e -> Log.w(TAG, "upload failed", e));
        task.addOnSuccessListener(taskSnapshot -> {
            // On success, caller should call saveFileMetadata with download url and meta
        });
        return ref;
    }

    public void saveFileMetadata(String docId, @NonNull Map<String, Object> metadata, OnCompleteListener callback) {
        manager.db().collection("files").document(docId).set(metadata).addOnCompleteListener(callback);
    }
}
