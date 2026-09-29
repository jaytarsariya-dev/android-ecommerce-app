package com.example.androidminorproject;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Wishlist extends AppCompatActivity {
    private RecyclerView recyclerWishlist;
    private WishlistAdapter wishlistAdapter;
    private List<ProductItem> wishlistItems;

    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerWishlist = findViewById(R.id.recyclerWishlist);
        recyclerWishlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        wishlistItems = new ArrayList<>();
        wishlistAdapter = new WishlistAdapter(this, wishlistItems);
        recyclerWishlist.setAdapter(wishlistAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fetchWishlist();
    }

    private void fetchWishlist() {
        db.collection("users").document(userId).collection("wishlist")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    wishlistItems.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        ProductItem item = document.toObject(ProductItem.class);
                        wishlistItems.add(item);
                    }
                    if (wishlistItems.isEmpty()) {
                        Toast.makeText(Wishlist.this, "Your wishlist is empty", Toast.LENGTH_SHORT).show();
                    }
                    wishlistAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(Wishlist.this, "Failed to fetch wishlist", Toast.LENGTH_SHORT).show());
    }
}
