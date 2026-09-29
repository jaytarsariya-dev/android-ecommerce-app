package com.example.androidminorproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartList;
    private FirebaseFirestore db;
    private String userId;
    private TextView totalText;
    private LinearLayout totalLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // Redirect to login activity if the user is not authenticated
            startActivity(new Intent(this, Loginforuser.class));
            finish();
            return;
        }
        userId = user.getUid();

        // Initialize UI elements
        recyclerView = findViewById(R.id.recyclerView);
        totalText = findViewById(R.id.totalText);
        totalLayout = findViewById(R.id.totallayout);

        // Set up RecyclerView with CartAdapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartList, userId, this);
        recyclerView.setAdapter(cartAdapter);

        db = FirebaseFirestore.getInstance();

        // Fetch cart items from Firestore when the activity is created
        loadCartData();

        // Handle Place Order button click
        Button placeOrderButton = findViewById(R.id.placeOrderButton);
        placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    // Fetch cart data from Firestore and populate the RecyclerView
    private void loadCartData() {
        db.collection("user_cart").document(userId).collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cartList.clear(); // Clear existing cart items
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        CartItem item = document.toObject(CartItem.class);
                        item.setProductId(document.getId());
                        cartList.add(item);
                    }
                    cartAdapter.notifyDataSetChanged();

                    // Show Toast if cart is empty
                    if (cartList.isEmpty()) {
                        totalText.setText("Total: ₹0");
                        totalLayout.setVisibility(View.VISIBLE); // Show total layout with ₹0
                        Toast.makeText(CartActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                    } else {
                        totalLayout.setVisibility(View.VISIBLE); // Show total layout
                        updateTotal(); // Update total if cart is not empty
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(CartActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Method to update the total of the cart
    public void updateTotal() {
        int total = 0;
        for (CartItem item : cartList) {
            total += item.getProductPrice() * item.getQuantity();
        }
        totalText.setText("Total: ₹" + total);
    }

    // Method to place the order and redirect to OrderDetailsActivity
    private void placeOrder() {
        // Check if cart is empty
        if (cartList.isEmpty()) {
            Toast.makeText(CartActivity.this, "Your cart is empty. Please add items to the cart.", Toast.LENGTH_SHORT).show();
            return; // Return early if cart is empty
        }

        int totalAmount = 0;
        ArrayList<String> productNames = new ArrayList<>();
        ArrayList<Integer> productQuantities = new ArrayList<>();
        ArrayList<Integer> productTotals = new ArrayList<>();

        for (CartItem item : cartList) {
            int itemTotal = item.getProductPrice() * item.getQuantity();
            totalAmount += itemTotal;

            // Store individual product details
            productNames.add(item.getProductName());
            productQuantities.add(item.getQuantity());
            productTotals.add(itemTotal);
        }

        // Pass the total amount and product details to OrderDetailsActivity
        Intent intent = new Intent(CartActivity.this, OrderDetailsActivity.class);
        intent.putExtra("cartTotal", totalAmount); // Passing total amount to OrderDetailsActivity
        intent.putStringArrayListExtra("productNames", productNames);
        intent.putIntegerArrayListExtra("productQuantities", productQuantities);
        intent.putIntegerArrayListExtra("productTotals", productTotals);

        startActivity(intent);
    }
}
