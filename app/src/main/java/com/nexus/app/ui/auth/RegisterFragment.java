package com.nexus.app.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nexus.app.R;
import com.nexus.app.data.remote.firebase.AuthManager;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment {
    private EditText etEmail, etPassword, etName;
    private ProgressBar progress;
    private Button btnRegister;
    private AuthManager authManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etEmail = view.findViewById(R.id.input_email);
        etPassword = view.findViewById(R.id.input_password);
        etName = view.findViewById(R.id.input_name);
        progress = view.findViewById(R.id.progress);
        btnRegister = view.findViewById(R.id.button_register);

        authManager = AuthManager.getInstance(requireContext());

        btnRegister.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString();
        String name = etName.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "All fields required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.length() < 6) {
            etPassword.setError("Password too weak");
            return;
        }

        progress.setVisibility(View.VISIBLE);
        authManager.register(email, pass, name, task -> {
            progress.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                FirebaseUser user = authManager.getCurrentUser();
                if (user != null) {
                    authManager.sendEmailVerification(user, verifyTask -> {
                        // show dialog / toast
                    });
                    Toast.makeText(getContext(), "Registered. Please verify your email.", Toast.LENGTH_LONG).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            } else {
                Toast.makeText(getContext(), "Registration failed: " + (task.getException() != null ? task.getException().getMessage() : "unknown"), Toast.LENGTH_LONG).show();
            }
        });
    }
}
