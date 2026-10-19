package com.nexus.app.data.remote.firebase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Centralized helper for Firebase Authentication operations.
 * Important: This class is not a replacement for the repository and ViewModel pattern but
 * provides a thin remote data source used by repositories.
 */
public class AuthManager {
    private static AuthManager instance;
    private final FirebaseAuth auth;

    private AuthManager(Context context) {
        auth = FirebaseAuth.getInstance();
    }

    public static AuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthManager(context.getApplicationContext());
        }
        return instance;
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void signIn(String email, String password, OnCompleteListener taskListener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(taskListener);
    }

    public void register(String email, String password, String displayName, OnCompleteListener taskListener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                // Set display name
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build();
                auth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(taskListener);
            } else {
                taskListener.onComplete(task);
            }
        });
    }

    public void sendEmailVerification(FirebaseUser user, OnCompleteListener taskListener) {
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(taskListener);
        } else {
            Task<Void> t = com.google.android.gms.tasks.Tasks.forException(new IllegalStateException("User null"));
            t.addOnCompleteListener(taskListener);
        }
    }

    public void sendPasswordReset(String email, OnCompleteListener taskListener) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(taskListener);
    }

    public void signOut() {
        auth.signOut();
    }
}
