package com.example.androidminorproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SupportFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore firestore;
    private List<ContactData> contactList;
    private ContactAdapter contactAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        // Initialize Firestore and RecyclerView
        firestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        contactList = new ArrayList<>();
        contactAdapter = new ContactAdapter(getContext(), contactList);  // Pass context to adapter

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(contactAdapter);

        // Fetch data from Firestore
        loadContacts();

        return view;
    }

    // Fetch contact data from Firestore
    private void loadContacts() {
        firestore.collection("ContactUs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    contactList.clear();  // Clear the list to avoid duplicates

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        ContactData contactData = document.toObject(ContactData.class);  // Convert Firestore document to ContactData model
                        contactList.add(contactData);  // Add contact data to the list
                    }

                    contactAdapter.notifyDataSetChanged();  // Notify the adapter that data has been updated
                })
                .addOnFailureListener(e -> {
                    // Handle failure case
                    Toast.makeText(getContext(), "Failed to load data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
