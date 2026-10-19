package com.nexus.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nexus.app.MainActivity;
import com.nexus.app.ui.auth.AuthActivity;
import com.nexus.app.ui.onboarding.OnboardingActivity;
import com.nexus.app.R;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final long SPLASH_DELAY = 800;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        boolean firstLaunch = getSharedPreferences("nexus_prefs", MODE_PRIVATE)
                .getBoolean("onboarding_complete", false);

        new Handler(getMainLooper()).postDelayed(() -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (!firstLaunch) {
                // Show onboarding
                startActivity(new Intent(this, OnboardingActivity.class));
            } else if (user == null) {
                // Not signed in
                startActivity(new Intent(this, AuthActivity.class));
            } else {
                // Signed in — ensure email verification check will happen inside MainActivity or other flow
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
        }, SPLASH_DELAY);
    }
}
