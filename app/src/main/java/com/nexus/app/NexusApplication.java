package com.nexus.app;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

public class NexusApplication extends Application {
    private static final String TAG = "NexusApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        try {
            FirebaseApp.initializeApp(this);
        } catch (Exception e) {
            Log.w(TAG, "Firebase initialization failed: " + e.getMessage());
        }

        // Initialize singletons, DI, database, etc. (AppDatabase will be created when needed)
    }
}
