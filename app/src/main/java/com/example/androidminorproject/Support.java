package com.example.androidminorproject;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Support extends AppCompatActivity {

    private TextView messageTextView, replyTextView;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        // Initialize Firebase Firestore, FirebaseAuth, and UI elements
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        messageTextView = findViewById(R.id.messageTextView);
        replyTextView = findViewById(R.id.replyTextView);

        // Get the authenticated user's email
        String userEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : null;

        if (userEmail != null) {
            // Load message and reply for the specific email
            loadMessageAndReply(userEmail);
        } else {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMessageAndReply(String userEmail) {
        // Query Firestore for the document where email field matches the authenticated user's email
        firestore.collection("ContactUs")
                .whereEqualTo("email", userEmail) // Assuming the email field is called 'email' in Firestore
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Assuming only one document per email, we take the first document
                        QueryDocumentSnapshot documentSnapshot = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);

                        String message = documentSnapshot.getString("message");
                        String reply = documentSnapshot.getString("reply");

                        // Check if there's no message or reply
                        if (message == null || message.isEmpty()) {
                            messageTextView.setText(""); // Clear the message
                            replyTextView.setText(""); // Clear the reply
                            Toast.makeText(Support.this, "No message available for you.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Set the message and reply in the TextViews in key-value format
                            messageTextView.setText("Message : " + (message != null ? message : "No message available"));
                            replyTextView.setText("Reply : " + (reply != null ? reply : "No reply available"));
                        }
                    } else {
                        messageTextView.setText(""); // Clear the message
                        replyTextView.setText(""); // Clear the reply
                        Toast.makeText(Support.this, "No document found for this email.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    messageTextView.setText(""); // Clear the message
                    replyTextView.setText(""); // Clear the reply
                    Toast.makeText(Support.this, "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
