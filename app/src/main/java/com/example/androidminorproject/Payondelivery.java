package com.example.androidminorproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Payondelivery extends AppCompatActivity {

    private Button confirmButton;
    private EditText mobileNumberEditText, addressEditText;
    private int total;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payondelivery);

        confirmButton = findViewById(R.id.confirmOrderButton);
        mobileNumberEditText = findViewById(R.id.mobileNumberEditText);
        addressEditText = findViewById(R.id.addressEditText);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, Loginforuser.class));
            finish();
            return;
        }
        userId = user.getUid();
        total = getIntent().getIntExtra("cartTotal", 0);

        confirmButton.setOnClickListener(v -> confirmOrder());
    }

    private void confirmOrder() {
        if (!validateInputs()) return;

        int orderId = getNextOrderId();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String mobileNumber = mobileNumberEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        Map<String, Object> orderData = new HashMap<>();
        orderData.put("orderId", String.valueOf(orderId));
        orderData.put("paymentMethod", "Pay On Delivery");
        orderData.put("total", total);
        orderData.put("date", currentDate);
        orderData.put("status", "Confirmed");
        orderData.put("userId", userId);
        orderData.put("mobileNumber", mobileNumber);
        orderData.put("address", address);

        db.collection("orders").document(String.valueOf(orderId))
                .set(orderData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    emptyCart(() -> {
                        Toast.makeText(this, "Order Confirmed! Pay on Delivery.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Payondelivery.this, MyOrders.class));
                        finish();
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private int getNextOrderId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyOrders", MODE_PRIVATE);
        int currentId = sharedPreferences.getInt("lastOrderId", 0) + 1;
        sharedPreferences.edit().putInt("lastOrderId", currentId).apply();
        return currentId;
    }

    private void emptyCart(Runnable onSuccess) {
        db.collection("user_cart").document(userId).collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        db.collection("user_cart").document(userId).collection("cart")
                                .document(document.getId()).delete();
                    }
                    onSuccess.run();
                })
                .addOnFailureListener(e -> Toast.makeText(Payondelivery.this, "Error clearing cart: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private boolean validateInputs() {
        if (mobileNumberEditText.getText().toString().trim().length() != 10) {
            Toast.makeText(this, "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(addressEditText.getText().toString().trim())) {
            Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
