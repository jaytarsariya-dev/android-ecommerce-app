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
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GooglePayActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_google_pay);

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
        productNames = getIntent().getStringArrayListExtra("productNames");
        productQuantities = getIntent().getIntegerArrayListExtra("productQuantities");
        productTotals = getIntent().getIntegerArrayListExtra("productTotals");

        confirmButton.setOnClickListener(v -> {
            String utrNumber = utrEditText.getText().toString().trim();

            if (utrNumber.length() != 12) {
                Toast.makeText(this, "Enter a valid 12-digit UTR number", Toast.LENGTH_SHORT).show();
                return;
            }

            int orderId = getNextOrderId();
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Order newOrder = new Order(String.valueOf(orderId), utrNumber, total, currentDate, "Confirmed", productNames, productQuantities, productTotals);

            saveOrderToSharedPreferences(newOrder);
            emptyCart();

            Toast.makeText(this, "Payment Confirmed!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(GooglePayActivity.this, MyOrders.class);
            startActivity(intent);
            finish();
        });
    }

    private int getNextOrderId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyOrders", MODE_PRIVATE);
        int currentId = sharedPreferences.getInt("lastOrderId", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lastOrderId", currentId + 1);
        editor.apply();
        return currentId + 1;
    }

    private void saveOrderToSharedPreferences(Order order) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyOrders", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Order>>() {}.getType();
        List<Order> ordersList = gson.fromJson(sharedPreferences.getString("ordersList", "[]"), type);
        if (ordersList == null) {
            ordersList = new ArrayList<>();
        }
        ordersList.add(order);
        editor.putString("ordersList", gson.toJson(ordersList));
        editor.apply();
    }

    private void emptyCart() {
        db.collection("user_cart").document(userId).collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        db.collection("user_cart").document(userId).collection("cart")
                                .document(document.getId())
                                .delete()
                                .addOnFailureListener(e ->
                                        Toast.makeText(GooglePayActivity.this, "Error clearing cart: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(GooglePayActivity.this, "Error fetching cart items: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}