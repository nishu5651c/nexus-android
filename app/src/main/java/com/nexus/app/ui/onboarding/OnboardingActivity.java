package com.nexus.app.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.nexus.app.R;
import com.nexus.app.ui.auth.AuthActivity;

public class OnboardingActivity extends AppCompatActivity {

    Button btnContinue, btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        btnContinue = findViewById(R.id.onboarding_continue);
        btnSkip = findViewById(R.id.onboarding_skip);

        btnSkip.setOnClickListener(v -> finishOnboarding());
        btnContinue.setOnClickListener(v -> finishOnboarding());
    }

    private void finishOnboarding() {
        getSharedPreferences("nexus_prefs", MODE_PRIVATE)
                .edit().putBoolean("onboarding_complete", true).apply();

        // After onboarding, go to auth
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }
}
