package com.example.androidminorproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QRCodePaymentActivity extends AppCompatActivity {

    private EditText utrEditText;
    private Button confirmButton;
    private int total;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;
    private List<String> productNames;
    private List<Integer> productQuantities;
    private List<Integer> productTotals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_pay);

        utrEditText = findViewById(R.id.utrEditText);
        confirmButton = findViewById(R.id.confirmButton);
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
        productNames = new ArrayList<>();
        productQuantities = new ArrayList<>();
        productTotals = new ArrayList<>();

        confirmButton.setOnClickListener(v -> fetchCartItems());
    }

    // Fetch products from cart before processing payment
    private void fetchCartItems() {
        db.collection("user_cart").document(userId).collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productNames.clear();
                    productQuantities.clear();
                    productTotals.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String name = document.getString("productName");
                        int quantity = document.getLong("quantity").intValue();
                        int total = document.getLong("totalPrice").intValue();

                        productNames.add(name);
                        productQuantities.add(quantity);
                        productTotals.add(total);
                    }

                    if (productNames.isEmpty()) {
                        Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                    } else {
                        processPayment();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(QRCodePaymentActivity.this, "Failed to fetch cart: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Process the payment and save the order
    private void processPayment() {
        String utrNumber = utrEditText.getText().toString().trim();

        if (!isValidUTR(utrNumber)) {
            Toast.makeText(this, "Enter a valid 12-digit UTR number", Toast.LENGTH_SHORT).show();
            return;
        }

        int orderId = getNextOrderId();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Order newOrder = new Order(
                String.valueOf(orderId),
                utrNumber,
                total,
                currentDate,
                "Confirmed",
                productNames,
                productQuantities,
                productTotals
        );

        saveOrderToSharedPreferences(newOrder);
        emptyCart();

        Toast.makeText(this, "Payment Confirmed!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(QRCodePaymentActivity.this, MyOrders.class));
        finish();
    }

    // Validate UTR Number
    private boolean isValidUTR(String utr) {
        return utr.length() == 12 && utr.matches("\\d+");
    }

    // Generate next Order ID
    private int getNextOrderId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyOrders", MODE_PRIVATE);
        int currentId = sharedPreferences.getInt("lastOrderId", 0);
        sharedPreferences.edit().putInt("lastOrderId", currentId + 1).apply();
        return currentId + 1;
    }

    // Save order to SharedPreferences
    private void saveOrderToSharedPreferences(Order order) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyOrders", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String existingOrdersJson = sharedPreferences.getString("ordersList", "[]");
        Type type = new TypeToken<List<Order>>() {}.getType();
        List<Order> ordersList = gson.fromJson(existingOrdersJson, type);

        if (ordersList == null) ordersList = new ArrayList<>();
        ordersList.add(order);

        editor.putString("ordersList", gson.toJson(ordersList));
        editor.apply();
    }

    // Empty the cart after payment
    private void emptyCart() {
        db.collection("user_cart").document(userId).collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        db.collection("user_cart").document(userId).collection("cart")
                                .document(document.getId()).delete();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(QRCodePaymentActivity.this, "Error clearing cart: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
