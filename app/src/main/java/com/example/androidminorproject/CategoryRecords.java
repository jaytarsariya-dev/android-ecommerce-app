package com.example.androidminorproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecords extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter1 adapter;
    private List<ProductItem> productList;
    private List<ProductItem> filteredList; // List for filtering
    private FirebaseFirestore firestore;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_records);

        String categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        recyclerView = findViewById(R.id.recordsRecyclerView);
        searchView = findViewById(R.id.searchView);

        // Set RecyclerView to use GridLayoutManager with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productList = new ArrayList<>();
        filteredList = new ArrayList<>();

        adapter = new MyAdapter1(this, filteredList, productItem -> {
            // Handle item click to open DetailActivity1
            Intent intent = new Intent(CategoryRecords.this, DetailActivity1.class);
            intent.putExtra("productName", productItem.getProductName());
            intent.putExtra("productDescription", productItem.getProductDescription());
            intent.putExtra("productPrice", productItem.getProductPrice());
            intent.putExtra("productImage", productItem.getImageUrl());  // Assuming productImage contains the image URL
            intent.putExtra("productCategory", productItem.getCategory());  // Pass the category here
            intent.putExtra("productId", productItem.getProductId());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();

        loadCategoryRecords(categoryName);
        setupSearchView();
    }

    private void loadCategoryRecords(String category) {
        firestore.collection("products")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ProductItem product = document.toObject(ProductItem.class);
                            productList.add(product);
                        }
                        // Initially display all products
                        filteredList.clear();
                        filteredList.addAll(productList);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // No action needed on submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRecords(newText);
                return true;
            }
        });
    }

    private void filterRecords(String query) {
        filteredList.clear();

        if (TextUtils.isEmpty(query)) {
            // If query is empty, show all products
            filteredList.addAll(productList);
        } else {
            // Filter products by name
            for (ProductItem product : productList) {
                if (product.getProductName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}
