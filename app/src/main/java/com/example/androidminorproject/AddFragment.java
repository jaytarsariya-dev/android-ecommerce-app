package com.example.androidminorproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddFragment extends Fragment {

    private EditText edtProductName, edtProductPrice, edtProductDescription,edtProductId,edtQuantity;
    private Spinner categorySpinner;
    private Button btnAdd;
    private ImageView productImage;
    private Uri imageUri; // For storing selected image URI
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private String selectedCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // Initialize Firestore and Storage reference
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("product_images");

        // Initialize UI components
        edtProductName = view.findViewById(R.id.edt_product_name);
        edtProductPrice = view.findViewById(R.id.edt_product_price);
        edtProductDescription = view.findViewById(R.id.edt_product_des);
        edtProductId=view.findViewById(R.id.edt_product_id);
        btnAdd = view.findViewById(R.id.btn_add);
        productImage = view.findViewById(R.id.product_image);
        categorySpinner = view.findViewById(R.id.category);
        edtQuantity = view.findViewById(R.id.edt_product_quantity);

        // Set image click listener to pick an image from the gallery
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Set button click listener to add product
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadImageAndAddProduct();
                } else {
                    Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setup spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.categories,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = null;
            }
        });

        return view;
    }

    // Open image picker to select image
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    // Handle image selection result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            imageUri = data.getData();
            productImage.setImageURI(imageUri); // Set the selected image in ImageView
        }
    }

    // Upload image to Firebase Storage and add product details to Firestore
    private void uploadImageAndAddProduct() {
        String productName = edtProductName.getText().toString();
        String productPrice = edtProductPrice.getText().toString();
        String productDescription = edtProductDescription.getText().toString();
        String productQuantity = edtQuantity.getText().toString(); // Get quantity

        // Check if fields are empty
        if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(productPrice) || TextUtils.isEmpty(productDescription) || TextUtils.isEmpty(productQuantity) || selectedCategory == null) {
            Toast.makeText(getActivity(), "Please fill all fields, including quantity, and select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get image download URL
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                // Add product item to Firestore with image URL
                                addProductItem(productName, productPrice, productDescription, productQuantity, imageUrl, selectedCategory);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Add product item to Firestore
    // Add product item to Firestore with the productId
    private void addProductItem(String productName, String productPrice, String productDescription, String productQuantity, String imageUrl, String category) {
        DocumentReference newProductRef = db.collection("products").document();  // Firestore auto-generates the ID
        String productId = newProductRef.getId(); // Generate a product ID

        // Create a HashMap to store product details
        Map<String, Object> productData = new HashMap<>();
        productData.put("productId", productId);
        productData.put("productName", productName);
        productData.put("productPrice", productPrice);
        productData.put("productDescription", productDescription);
        productData.put("productQuantity", productQuantity); // Add quantity
        productData.put("imageUrl", imageUrl);
        productData.put("category", category);

        newProductRef.set(productData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Product item added", Toast.LENGTH_SHORT).show();
                            // Clear input fields after success
                            edtProductName.setText("");
                            edtProductPrice.setText("");
                            edtProductDescription.setText("");
                            edtQuantity.setText(""); // Clear quantity field
                            edtProductId.setText("");

                            productImage.setImageResource(R.drawable.add_image);  // Reset to default image
                            categorySpinner.setSelection(0); // Reset spinner
                        } else {
                            Toast.makeText(getActivity(), "Error adding item", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
