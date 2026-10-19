package com.nexus.app.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.nexus.app.data.local.db.AppDatabase;
import com.nexus.app.data.local.model.UserEntity;
import com.nexus.app.data.remote.firebase.AuthManager;
import com.google.firebase.auth.FirebaseUser;

/**
 * Simple repository managing local and remote user data.
 * This will be expanded later with Firestore sync, presence, etc.
 */
public class UserRepository {
    private final AppDatabase db;
    private final AuthManager authManager;

    public UserRepository(Context context) {
        db = AppDatabase.getInstance(context);
        authManager = AuthManager.getInstance(context);
    }

    public FirebaseUser getCurrentUser() {
        return authManager.getCurrentUser();
    }

    public void saveLocalUser(UserEntity user) {
        new InsertTask(db).execute(user);
    }

    private static class InsertTask extends AsyncTask<UserEntity, Void, Void> {
        private final AppDatabase db;
        InsertTask(AppDatabase db) { this.db = db; }
        @Override
        protected Void doInBackground(UserEntity... users) {
            db.userDao().upsert(users[0]);
            return null;
        }
    }
}
