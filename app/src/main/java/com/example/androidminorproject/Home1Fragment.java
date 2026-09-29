// Filename: Home1Fragment.java
package com.example.androidminorproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class Home1Fragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter1 myAdapter1;
    private List<FoodItem> foodItemList;
    private List<FoodItem> filteredList;
    private FirebaseFirestore db;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize Lists and Adapter
        foodItemList = new ArrayList<>();
        filteredList = new ArrayList<>();
//        myAdapter1 = new MyAdapter1(getActivity(), filteredList, this::onItemClick);
        recyclerView.setAdapter(myAdapter1);

        // Initialize SearchView
        searchView = view.findViewById(R.id.search);

        // Load food items from Firestore
        loadFoodItems();

        // Setup SearchView functionality
        setupSearchView();

        return view;
    }

    private void onItemClick(ProductItem productItem) {
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);  // Filter when query is submitted
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);  // Filter as the text changes
                return true;
            }
        });
    }

    // Filter food items based on foodName
    private void filter(String text) {
        filteredList.clear();  // Clear the filtered list first
        if (TextUtils.isEmpty(text)) {
            // If no text, show all items
            filteredList.addAll(foodItemList);
        } else {
            // Filter by food name (case insensitive)
            for (FoodItem item : foodItemList) {
                if (item.getFoodName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }

        // Notify the adapter to update the RecyclerView
        myAdapter1.notifyDataSetChanged();
    }

    // Load all food items from Firestore into the foodItemList
    private void loadFoodItems() {
        db.collection("foods")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            // Show a Toast message in case of Firestore errors
                            Toast.makeText(getActivity(), "Error loading data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        foodItemList.clear();  // Clear the list before adding new data
                        for (QueryDocumentSnapshot document : value) {
                            FoodItem foodItem = document.toObject(FoodItem.class);
                            foodItemList.add(foodItem);
                        }

                        // By default, show all items in the filtered list
                        filteredList.clear();
                        filteredList.addAll(foodItemList);
                        myAdapter1.notifyDataSetChanged();
                    }
                });
    }

    // Handle item click
    private void onItemClick(FoodItem foodItem) {
        // Handle click event, for example, navigating to DetailActivity1
        Intent intent = new Intent(getActivity(), DetailActivity1.class);
        intent.putExtra("Name", foodItem.getFoodName());
        intent.putExtra("Description", foodItem.getFoodDescription());
        intent.putExtra("Price", foodItem.getFoodPrice());
        intent.putExtra("Image", foodItem.getImageUrl());
        intent.putExtra("Id", foodItem.getFoodId());
        startActivity(intent);
    }
}
