package com.example.androidminorproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private RecyclerView userRecyclerView;
    private FirebaseFirestore db;
    private UserAdapter userAdapter;
    private List<String> emailList;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance(); // Initialize Firestore instance
        emailList = new ArrayList<>(); // Initialize email list
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Initialize RecyclerView
        userRecyclerView = view.findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Pass the Remove button action to the adapter
        userAdapter = new UserAdapter(emailList, email -> removeUserFromFirestore(email));
        userRecyclerView.setAdapter(userAdapter);

        // Fetch and display emails
        fetchUserEmails();

        return view;
    }

    private void fetchUserEmails() {
        // Fetch documents from the 'users' collection
        db.collection("users").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        emailList.clear();
                        if (querySnapshot != null) {
                            // Iterate through the documents
                            for (DocumentSnapshot document : querySnapshot) {
                                // Fetch the email associated with the userId field
                                String email = document.getString("email"); // Fetch "email" field
                                if (email != null) {
                                    emailList.add(email); // Add email to the list
                                }
                            }
                            userAdapter.notifyDataSetChanged(); // Update RecyclerView
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to load emails", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeUserFromFirestore(String email) {
        // Get the document reference for the user with the specific email
        db.collection("users")
                .whereEqualTo("email", email) // assuming "email" is the identifier for the user
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            // Delete the document with the matched email
                            db.collection("users").document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Remove the email from the local list and update RecyclerView
                                        emailList.remove(email);
                                        userAdapter.notifyDataSetChanged();
                                        Toast.makeText(getContext(), "User removed", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "Failed to remove user", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                });
    }
}
