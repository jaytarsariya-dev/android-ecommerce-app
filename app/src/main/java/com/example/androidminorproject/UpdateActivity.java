package com.example.androidminorproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    private ImageView updateImage, imgback;
    private Button updateButton;
    private EditText updateDesc, updateName, updatePrice, updateId, updateCategory;
    private String productName, productDescription, productPrice, productId, productCategory;
    private String imageUrl;
    private String oldImageURL;
    private Uri uri;

    private DocumentReference documentReference;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Initialize UI components
        updateButton = findViewById(R.id.updateButton);
        updateDesc = findViewById(R.id.updateDesc);
        updateImage = findViewById(R.id.updateImage);
        updatePrice = findViewById(R.id.updatePrice);
        updateName = findViewById(R.id.updateName);
        updateId = findViewById(R.id.updateId);
        updateCategory = findViewById(R.id.updateCategory);
        imgback = findViewById(R.id.back2);

        // Handle back button action
        imgback.setOnClickListener(v -> finish());

        // Initialize Firebase services
        firestore = FirebaseFirestore.getInstance();

        // Retrieve data passed via intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Glide.with(this).load(bundle.getString("Image")).into(updateImage);
            updateName.setText(bundle.getString("Name"));
            updateDesc.setText(bundle.getString("Description"));
            updatePrice.setText(bundle.getString("Price"));
            updateCategory.setText(bundle.getString("Category"));
            updateId.setText(bundle.getString("Id"));
            oldImageURL = bundle.getString("Image");
            productId = bundle.getString("Id");
        }

        // Get reference to the specific document in Firestore
        documentReference = firestore.collection("products").document(productId);

        // Image picker intent and handling the result
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        uri = data.getData();
                        updateImage.setImageURI(uri);
                    } else {
                        Toast.makeText(this, "No Image Selected", Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Handle image change
        updateImage.setOnClickListener(view -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });

        // Handle update button click
        updateButton.setOnClickListener(view -> {
            if (uri != null) {
                saveDataWithNewImage();
            } else {
                updateDataWithoutImage();
            }
        });
    }

    // Upload new image and update fields (excluding ID)
    private void saveDataWithNewImage() {
        // Set up the new image upload reference
        storageReference = FirebaseStorage.getInstance().getReference().child("product_images")
                .child(uri.getLastPathSegment());

        // Upload the new image
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            // Get the download URL for the new image
            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    imageUrl = task.getResult().toString();
                    updateData(); // After the image is uploaded, update the data
                }
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    // Update Firestore data with a new image
    private void updateData() {
        // Get updated product details
        productName = updateName.getText().toString().trim();
        productDescription = updateDesc.getText().toString().trim();
        productPrice = updatePrice.getText().toString();
        productCategory = updateCategory.getText().toString();

        // Create a map with updated fields (excluding ID)
        Map<String, Object> productItemUpdate = new HashMap<>();
        productItemUpdate.put("productName", productName);
        productItemUpdate.put("productDescription", productDescription);
        productItemUpdate.put("productPrice", productPrice);
        productItemUpdate.put("category", productCategory);
        productItemUpdate.put("imageUrl", imageUrl);

        // Update Firestore document
        documentReference.set(productItemUpdate, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Delete the old image after updating
                        if (oldImageURL != null && !oldImageURL.isEmpty()) {
                            deleteOldImage();
                        } else {
                            notifyUpdateSuccess();
                        }
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    // Delete the old image from Firebase Storage
    private void deleteOldImage() {
        StorageReference oldImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
        oldImageRef.delete().addOnSuccessListener(aVoid -> notifyUpdateSuccess())
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete old image: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    notifyUpdateSuccess(); // Proceed even if deletion fails
                });
    }

    // Update data without changing the image
    private void updateDataWithoutImage() {
        // Get updated product details
        productName = updateName.getText().toString().trim();
        productDescription = updateDesc.getText().toString().trim();
        productPrice = updatePrice.getText().toString();
        productCategory = updateCategory.getText().toString();

        // Map with updated fields (excluding ID)
        Map<String, Object> productItemUpdate = new HashMap<>();
        productItemUpdate.put("productName", productName);
        productItemUpdate.put("productDescription", productDescription);
        productItemUpdate.put("productPrice", productPrice);
        productItemUpdate.put("category", productCategory);
        productItemUpdate.put("imageUrl", oldImageURL); // Retain the old image

        // Update Firestore document
        documentReference.set(productItemUpdate, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notifyUpdateSuccess();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    // Notify the user of a successful update and redirect to Admin Panel
    private void notifyUpdateSuccess() {
        Toast.makeText(this, "Updated successfully", Toast.LENGTH_LONG).show();
        redirectToAdminPanel();
    }

    // Redirect to Admin Panel after a successful update
    private void redirectToAdminPanel() {
        Intent intent = new Intent(UpdateActivity.this, Adminpanel.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
