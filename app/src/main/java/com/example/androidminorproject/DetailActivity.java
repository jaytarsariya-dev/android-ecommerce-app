package com.example.androidminorproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    TextView productDesc, productName, productPrice, productCategory,productId,productQuantity;
    ImageView productImage, imgBack;
    Button deleteButton, editButton;

    String imageUrl = "";
    // Product ID to retrieve from the Intent for Firestore operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize views
        productDesc = findViewById(R.id.productDescription);
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productId = findViewById(R.id.productId);  // Initialize the TextView for productId
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        productPrice = findViewById(R.id.productPrice);
        productQuantity = findViewById(R.id.productQuantity);
        productCategory = findViewById(R.id.productCategory); // New TextView for category
        imgBack = findViewById(R.id.back);

        // Receive data from Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productDesc.setText(bundle.getString("Description"));
            productName.setText(bundle.getString("Name"));
            productPrice.setText(bundle.getString("Price"));
            productQuantity.setText(bundle.getString("Quantity"));
            productCategory.setText(bundle.getString("Category")); // Set category
            imageUrl = bundle.getString("Image");
            String productIdStr = bundle.getString("Id"); // Retrieve product ID from intent
            productId.setText(productIdStr); // Display productId in the TextView

            // Load image using Glide
            Glide.with(this).load(imageUrl).into(productImage);
        }

        // Back button functionality
        imgBack.setOnClickListener(v -> finish());

        // Delete product functionality
        deleteButton.setOnClickListener(view -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

            if (imageUrl != null && !imageUrl.isEmpty() && productId != null && !productId.getText().toString().isEmpty()) {
                // Delete image from Firebase Storage
                storageReference.delete().addOnSuccessListener(aVoid -> {
                    // Delete document from Firestore
                    db.collection("products").document(productId.getText().toString()).delete()
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(DetailActivity.this, "Product deleted successfully", Toast.LENGTH_LONG).show();
                                finish();
                            }).addOnFailureListener(e -> {
                                Toast.makeText(DetailActivity.this, "Failed to delete product: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                }).addOnFailureListener(e -> {
                    Toast.makeText(DetailActivity.this, "Failed to delete image: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            } else {
                Toast.makeText(DetailActivity.this, "Invalid product data", Toast.LENGTH_SHORT).show();
            }
        });

        // Edit product functionality
        editButton.setOnClickListener(view -> {
            Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                    .putExtra("Name", productName.getText().toString())
                    .putExtra("Description", productDesc.getText().toString())
                    .putExtra("Price", productPrice.getText().toString())
                    .putExtra("Category", productCategory.getText().toString()) // Pass category to UpdateActivity
                    .putExtra("Image", imageUrl)
                    .putExtra("Id", productId.getText().toString()); // Pass productId from TextView for editing
            startActivity(intent);
        });
    }
}
