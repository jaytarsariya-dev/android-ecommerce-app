package com.example.androidminorproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.MyViewHolder> {
    private Context context;
    private List<ProductItem> productList;
    private OnItemClickListener listener;

    private FirebaseFirestore db;
    private String userId;

    public MyAdapter1(Context context, List<ProductItem> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;

        // Initialize Firestore and get current user ID
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductItem product = productList.get(position);

        holder.productName.setText(product.getProductName());
        holder.productDescription.setText(product.getProductDescription());
        holder.productPrice.setText(product.getProductPrice());
        holder.productCategory.setText(product.getCategory());
        holder.productId.setText(product.getProductId());
        holder.productQuantity.setText(product.getQuantity());

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.add_image)
                .into(holder.productImage);

        // Set initial heart icon to gray
        holder.wishlistHeart.setImageResource(R.drawable.ic_heart_gray);

        // Handle heart icon click
        holder.wishlistHeart.setOnClickListener(v -> {
            holder.wishlistHeart.setImageResource(R.drawable.ic_heart_red); // Change icon to red
            addToWishlist(product); // Add item to Firestore
        });

        holder.itemView.setOnClickListener(view -> listener.onItemClick(product));
    }

    private void addToWishlist(ProductItem product) {
        // Reference to the user's wishlist collection
        CollectionReference wishlistRef = db.collection("users").document(userId).collection("wishlist");

        // Create a map for the product details
        Map<String, Object> wishlistItem = new HashMap<>();
        wishlistItem.put("productId", product.getProductId());
        wishlistItem.put("productName", product.getProductName());
        wishlistItem.put("productDescription", product.getProductDescription());
        wishlistItem.put("productPrice", product.getProductPrice());
        wishlistItem.put("category", product.getCategory());
        wishlistItem.put("quantity", product.getQuantity());
        wishlistItem.put("imageUrl", product.getImageUrl());

        // Add the product to Firestore
        wishlistRef.document(product.getProductId()).set(wishlistItem)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to add to Wishlist", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDescription, productPrice, productCategory, productId,productQuantity;
        ImageView productImage, wishlistHeart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productCategory = itemView.findViewById(R.id.productCategory);
            productId = itemView.findViewById(R.id.productId);
            productImage = itemView.findViewById(R.id.productImage);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            wishlistHeart = itemView.findViewById(R.id.wishlistHeart);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ProductItem product);
    }
}
