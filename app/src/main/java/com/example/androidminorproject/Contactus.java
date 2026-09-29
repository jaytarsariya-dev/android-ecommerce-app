package com.example.androidminorproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Contactus extends AppCompatActivity {

    private EditText emailEditText, messageEditText;
    private Button submitButton;

    // Firestore instance
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        emailEditText = findViewById(R.id.email_ul);
        messageEditText = findViewById(R.id.message_contact_us);
        submitButton = findViewById(R.id.login_u);

        // Set the click listener for the submit button
        submitButton.setOnClickListener(v -> {
            // Get the email and message entered by the user
            String email = emailEditText.getText().toString().trim();
            String message = messageEditText.getText().toString().trim();

            // Validate email and message
            if (!email.isEmpty() && !message.isEmpty()) {
                // Create a map to store data
                Map<String, Object> contactData = new HashMap<>();
                contactData.put("email", email);
                contactData.put("message", message); // Store the latest message

                // Use email as the document ID
                firestore.collection("ContactUs")
                        .document(email) // Set the email as the document ID
                        .set(contactData) // Overwrite or create the document
                        .addOnSuccessListener(aVoid -> {
                            // Show success message
                            Toast.makeText(Contactus.this, "Message sent successfully", Toast.LENGTH_SHORT).show();

                            // Clear the EditText fields
                            emailEditText.setText("");
                            messageEditText.setText("");
                        })
                        .addOnFailureListener(e -> {
                            // Show error message
                            Toast.makeText(Contactus.this, "Error sending message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Show error message if fields are empty
                Toast.makeText(Contactus.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
