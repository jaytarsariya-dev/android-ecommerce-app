package com.example.androidminorproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity1 extends AppCompatActivity {

    private TextView productName, productDescription, productPrice, productCategory;
    private ImageView productImage, backButton;
    private Button addToCartButton, placeOrderButton;
    private String productId;
    private FirebaseFirestore db;
    private int productPriceValue;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail1);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // Redirect to login activity if the user is not authenticated
            startActivity(new Intent(this, Loginforuser.class));
            finish();
            return;
        }
        String userId = user.getUid();

        // Initialize views
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        productCategory = findViewById(R.id.productCategory);
        productImage = findViewById(R.id.productImage);
        backButton = findViewById(R.id.back);
        addToCartButton = findViewById(R.id.cartbutton);
        placeOrderButton = findViewById(R.id.orderbutton);

        // Get data passed from Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("productName");
        String description = intent.getStringExtra("productDescription");
        String price = intent.getStringExtra("productPrice");
        String imageUrl = intent.getStringExtra("productImage");
        String category = intent.getStringExtra("productCategory");
        productId = intent.getStringExtra("productId");

        // Parse the price and set it to the productPrice value
        productPriceValue = Integer.parseInt(price);

        // Set data to views
        productName.setText(name);
        productDescription.setText(description);
        productPrice.setText("₹" + price);
        productCategory.setText(category);
        Glide.with(this).load(imageUrl).into(productImage);

        // Handle back button click
        backButton.setOnClickListener(view -> onBackPressed());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Add to Cart logic
        addToCartButton.setOnClickListener(view -> {
            // Check if the product already exists in the cart
            db.collection("user_cart").document(userId).collection("cart")
                    .whereEqualTo("productId", productId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Product already exists, update quantity
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            int currentQuantity = document.getLong("quantity").intValue();
                            int newQuantity = currentQuantity + 1;
                            int newTotalPrice = productPriceValue * newQuantity;

                            // Update the cart item with new quantity and total price
                            db.collection("user_cart").document(userId).collection("cart").document(document.getId())
                                    .update("quantity", newQuantity, "totalPrice", newTotalPrice)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(DetailActivity1.this, "Quantity updated", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(DetailActivity1.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        } else {
                            // Product does not exist in the cart, add a new item
                            Map<String, Object> cartItem = new HashMap<>();
                            cartItem.put("productId", productId);
                            cartItem.put("productName", name);
                            cartItem.put("productImage", imageUrl);
                            cartItem.put("productPrice", productPriceValue);
                            cartItem.put("quantity", 1);
                            cartItem.put("totalPrice", productPriceValue);

                            db.collection("user_cart").document(userId).collection("cart").document()
                                    .set(cartItem)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(DetailActivity1.this, "Added to Cart", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(DetailActivity1.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(DetailActivity1.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        // Place Order logic
        placeOrderButton.setOnClickListener(view -> {
            // Pass the product price (cart total) to OrderDetailsActivity
            Intent intent1 = new Intent(DetailActivity1.this, OrderDetailsActivity.class);
            intent1.putExtra("cartTotal", productPriceValue); // Send the product price as cart total
            startActivity(intent1);
        });
    }
}
