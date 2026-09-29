package com.example.androidminorproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private TextView txtLogin;
    private EditText edtName, edtEmail, edtPassword;
    private Button btnRegister;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        txtLogin = findViewById(R.id.logintext);
        edtName = findViewById(R.id.name_r);
        edtEmail = findViewById(R.id.email_r);
        edtPassword = findViewById(R.id.password_r);
        btnRegister = findViewById(R.id.register_u);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim().toLowerCase();
            String password = edtPassword.getText().toString().trim();

            // Input Validation
            if (name.isEmpty()) {
                edtName.setError("Please enter your name");
                return;
            }
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.setError("Please enter a valid email address");
                return;
            }
            if (password.isEmpty() || password.length() < 6) {
                edtPassword.setError("Password must be at least 6 characters");
                return;
            }

            // Create a new user in Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Get the User ID (UID)
                            String userId = auth.getCurrentUser().getUid();

                            // Create a user object to store in Firestore
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("userId", userId);  // Store the UID
                            userMap.put("name", name);
                            userMap.put("email", email);

                            // Save the user data in Firestore under the UID
                            db.collection("users").document(userId)
                                    .set(userMap)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            // Save user info in SharedPreferences after registration
                                            SharedPreferences prefs = getSharedPreferences("UserLoginPrefs", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putBoolean("isLoggedIn", true);
                                            editor.putString("loggedInUserEmail", email);
                                            editor.putString("userId", userId); // Save userId as well
                                            editor.apply();

                                            Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                            // Navigate to Login Activity
                                            startActivity(new Intent(getApplicationContext(), Loginforuser.class));
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Failed to save user info: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Redirect to Login Activity
        txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(Registration.this, Loginforuser.class));
            finish();
        });
    }
}