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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter1 myAdapter1;
    private List<ProductItem> productList;
    private List<ProductItem> filteredList;
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

        // Use GridLayoutManager to display items in a grid with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Add spacing between grid items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));

        // Initialize Lists and Adapter
        productList = new ArrayList<>();
        filteredList = new ArrayList<>();
        myAdapter1 = new MyAdapter1(getActivity(), filteredList, this::onItemClick);
        recyclerView.setAdapter(myAdapter1);

        // Initialize SearchView
        searchView = view.findViewById(R.id.search);

        // Load product items from Firestore
        loadProductItems();

        // Setup SearchView functionality
        setupSearchView();

        return view;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query); // Filter when query is submitted
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText); // Filter as the text changes
                return true;
            }
        });
    }

    // Filter product items based on productName
    private void filter(String text) {
        filteredList.clear(); // Clear the filtered list first
        if (TextUtils.isEmpty(text)) {
            // If no text, show all items
            filteredList.addAll(productList);
        } else {
            // Filter by product name (case insensitive)
            for (ProductItem item : productList) {
                if (item.getProductName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        // Notify the adapter to update the RecyclerView
        myAdapter1.notifyDataSetChanged();
    }

    // Load all product items from Firestore into the productList
    private void loadProductItems() {
        db.collection("products")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getActivity(), "Error loading data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        productList.clear();
                        for (QueryDocumentSnapshot document : value) {
                            ProductItem productItem = document.toObject(ProductItem.class);
                            productList.add(productItem);
                        }

                        filteredList.clear();
                        filteredList.addAll(productList);
                        myAdapter1.notifyDataSetChanged();
                    }
                });
    }


    // Handle item click
    private void onItemClick(ProductItem productItem) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("Id", productItem.getProductId()); // Pass productId here
        intent.putExtra("Name", productItem.getProductName());
        intent.putExtra("Description", productItem.getProductDescription());
        intent.putExtra("Price", productItem.getProductPrice());
        intent.putExtra("Category", productItem.getCategory());
        intent.putExtra("Image", productItem.getImageUrl());
        intent.putExtra("Quantity",productItem.getQuantity());
        startActivity(intent);
    }
}
