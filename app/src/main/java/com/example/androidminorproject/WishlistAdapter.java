package com.example.androidminorproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    private final Context context;
    private final List<ProductItem> wishlistItems;

    public WishlistAdapter(Context context, List<ProductItem> wishlistItems) {
        this.context = context;
        this.wishlistItems = wishlistItems;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wishlist_item, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        ProductItem product = wishlistItems.get(position);

        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(String.format("₹%s", product.getProductPrice()));
        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);

        // Click listener for removing an item from the wishlist
        holder.removeButton.setOnClickListener(v -> removeItem(position));

        // Click listener to open the DetailActivity1 and pass product details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity1.class);
            intent.putExtra("productName", product.getProductName());
            intent.putExtra("productDescription", product.getProductDescription());
            intent.putExtra("productPrice", String.valueOf(product.getProductPrice()));
            intent.putExtra("productCategory", product.getCategory());
            intent.putExtra("productImage", product.getImageUrl());
            intent.putExtra("productId", product.getProductId());
            context.startActivity(intent);
        });
    }

    private void removeItem(int position) {
        ProductItem product = wishlistItems.get(position);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userId).collection("wishlist")
                .document(product.getProductId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    wishlistItems.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Item removed from wishlist", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    public static class WishlistViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView productImage;
        ImageButton removeButton;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
