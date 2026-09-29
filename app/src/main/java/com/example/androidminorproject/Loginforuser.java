package com.example.androidminorproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Loginforuser extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txtRegister, txtForgotPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    private static final String PREF_NAME = "UserLoginPrefs";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String LOGGED_IN_USER_EMAIL = "loggedInUserEmail";
    private static final String LOGGED_IN_USER_ID = "userId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginforuser);

        edtEmail = findViewById(R.id.email_ul);
        edtPassword = findViewById(R.id.password_ul);
        btnLogin = findViewById(R.id.login_u);
        txtRegister = findViewById(R.id.registraiontext_u);
        txtForgotPassword = findViewById(R.id.forgetpass);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean(IS_LOGGED_IN, false);
        String loggedInEmail = prefs.getString(LOGGED_IN_USER_EMAIL, "");

        if (isLoggedIn) {
            // User is already logged in, redirect to User activity
            Toast.makeText(this, "Welcome back, " + loggedInEmail, Toast.LENGTH_SHORT).show();
            navigateToUserActivity();
            return; // Prevent further execution of onCreate
        }

        // Static credential check for admin
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString();

            if (validateInputs(email, password)) {
                signInWithFirebase(email, password);
            }
        });

        // Redirect to registration activity
        txtRegister.setOnClickListener(v -> navigateToRegistrationActivity());

        // Redirect to forgot password activity
        txtForgotPassword.setOnClickListener(v -> navigateToForgotPasswordActivity());
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            edtEmail.setError("Email cannot be empty");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Invalid email address");
            return false;
        }
        if (password.isEmpty()) {
            edtPassword.setError("Password cannot be empty");
            return false;
        }
        return true;
    }

    private void signInWithFirebase(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    progressBar.setVisibility(View.GONE);

                    // Save login state and user email in SharedPreferences
                    SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(IS_LOGGED_IN, true);
                    editor.putString(LOGGED_IN_USER_EMAIL, email);
                    editor.putString(LOGGED_IN_USER_ID, auth.getCurrentUser().getUid()); // Save userId
                    editor.apply();

                    // Redirect to User activity
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    navigateToUserActivity();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToUserActivity() {
        Intent intent = new Intent(getApplicationContext(), User.class);
        startActivity(intent);
        finish();
    }

    private void navigateToRegistrationActivity() {
        Intent intent = new Intent(getApplicationContext(), Registration.class);
        startActivity(intent);
        finish();
    }

    private void navigateToForgotPasswordActivity() {
        Intent intent = new Intent(getApplicationContext(), Forgot.class);
        startActivity(intent);
        finish();
    }
};