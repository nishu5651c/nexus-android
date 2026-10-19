package com.nexus.app.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nexus.app.R;
import com.nexus.app.data.remote.firebase.AuthManager;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {
    private EditText etEmail, etPassword;
    private ProgressBar progress;
    private Button btnLogin;
    private TextView tvGoRegister, tvForgot;

    private AuthManager authManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etEmail = view.findViewById(R.id.input_email);
        etPassword = view.findViewById(R.id.input_password);
        progress = view.findViewById(R.id.progress);
        btnLogin = view.findViewById(R.id.button_login);
        tvGoRegister = view.findViewById(R.id.text_register);
        tvForgot = view.findViewById(R.id.text_forgot);

        authManager = AuthManager.getInstance(requireContext());

        btnLogin.setOnClickListener(v -> attemptLogin());
        tvGoRegister.setOnClickListener(v -> {
            if (getActivity() instanceof com.nexus.app.ui.auth.AuthActivity) {
                ((com.nexus.app.ui.auth.AuthActivity) getActivity()).showFragment(new RegisterFragment());
            }
        });

        tvForgot.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Enter your email first to reset", Toast.LENGTH_SHORT).show();
                return;
            }
            authManager.sendPasswordReset(email, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Password reset email sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed: " + (task.getException() != null ? task.getException().getMessage() : "unknown"), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email required");
            return;
        }
        if (TextUtils.isEmpty(pass) || pass.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        progress.setVisibility(View.VISIBLE);
        authManager.signIn(email, pass, (task) -> {
            progress.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                FirebaseUser user = authManager.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    // Launch main
                    requireActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Please verify your email before continuing", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "Login failed: " + (task.getException() != null ? task.getException().getMessage() : "unknown"), Toast.LENGTH_LONG).show();
            }
        });
    }
}
