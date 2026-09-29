package com.example.androidminorproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartList;
    private String userId;
    private FirebaseFirestore db;
    private CartActivity cartActivity;

    public CartAdapter(List<CartItem> cartList, String userId, CartActivity cartActivity) {
        this.cartList = cartList;
        this.userId = userId;
        this.db = FirebaseFirestore.getInstance();
        this.cartActivity = cartActivity;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem item = cartList.get(position);

        holder.productName.setText(item.getProductName());
        holder.productPrice.setText("₹" + item.getProductPrice());
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        // Calculate individual total
        int totalProductPrice = item.getProductPrice() * item.getQuantity();
        holder.totalPrice.setText("₹" + totalProductPrice);

        // Load product image (if available)
        Glide.with(holder.itemView.getContext())
                .load(item.getProductImage())
                .into(holder.productImage);

        // Update item quantity
        holder.increaseButton.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            item.setTotalPrice(item.getProductPrice() * item.getQuantity());
            updateCartItem(item);
            notifyItemChanged(position);
            cartActivity.updateTotal(); // Update overall total
        });

        holder.decreaseButton.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                item.setTotalPrice(item.getProductPrice() * item.getQuantity());
                updateCartItem(item);
                notifyItemChanged(position);
                cartActivity.updateTotal(); // Update overall total
            } else {
                // Remove item from Firestore when quantity reaches 0
                removeCartItem(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    // Method to update a cart item in Firestore
    private void updateCartItem(CartItem item) {
        db.collection("user_cart").document(userId).collection("cart")
                .document(item.getProductId())
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    // Data successfully updated in Firestore
                })
                .addOnFailureListener(e -> {
                    // Handle failure to update data
                });
    }

    // Method to remove a cart item from Firestore
    private void removeCartItem(CartItem item, int position) {
        db.collection("user_cart").document(userId).collection("cart")
                .document(item.getProductId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    cartList.remove(position);
                    notifyItemRemoved(position);
                    cartActivity.updateTotal(); // Update overall total
                })
                .addOnFailureListener(e -> {
                    // Handle error if needed
                });
    }

    // ViewHolder class
    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, quantity, totalPrice;
        ImageView productImage;
        Button increaseButton, decreaseButton;

        public CartViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            productImage = itemView.findViewById(R.id.productImage);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }
    }
}
