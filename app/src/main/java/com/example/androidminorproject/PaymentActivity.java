package com.example.androidminorproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    private TextView totalTextView;
    private LinearLayout googlePayButton, phonePayButton, qrCodeButton, payOnDeliveryButton;
    private int cartTotal;
    private String email, mobile, locality, address;
    private ArrayList<String> productNames;
    private ArrayList<Integer> productQuantities, productTotals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize UI elements
        totalTextView = findViewById(R.id.totalTextView);
        googlePayButton = findViewById(R.id.gp);
        phonePayButton = findViewById(R.id.pp);
        qrCodeButton = findViewById(R.id.qrcode);
        payOnDeliveryButton = findViewById(R.id.pod);

        // Retrieve all data from Intent
        cartTotal = getIntent().getIntExtra("cartTotal", 0);
        email = getIntent().getStringExtra("email");
        mobile = getIntent().getStringExtra("mobile");
        locality = getIntent().getStringExtra("locality");
        address = getIntent().getStringExtra("address");
        productNames = getIntent().getStringArrayListExtra("productNames");
        productQuantities = getIntent().getIntegerArrayListExtra("productQuantities");
        productTotals = getIntent().getIntegerArrayListExtra("productTotals");

        // Display the total of the cart at the top
        totalTextView.setText("Total: ₹" + cartTotal);

        // Set up listeners for each payment option
        googlePayButton.setOnClickListener(v -> redirectToPayment(GooglePayActivity.class));
        phonePayButton.setOnClickListener(v -> redirectToPayment(PhonePayActivity.class));
        qrCodeButton.setOnClickListener(v -> redirectToPayment(QRCodePaymentActivity.class));
        payOnDeliveryButton.setOnClickListener(v -> redirectToPayment(Payondelivery.class));
    }

    // Generic method to redirect to any payment activity
    private void redirectToPayment(Class<?> targetActivity) {
        if (cartTotal > 0) {
            Intent intent = new Intent(PaymentActivity.this, targetActivity);
            intent.putExtra("cartTotal", cartTotal);
            intent.putExtra("email", email);
            intent.putExtra("mobile", mobile);
            intent.putExtra("locality", locality);
            intent.putExtra("address", address);
            intent.putStringArrayListExtra("productNames", productNames);
            intent.putIntegerArrayListExtra("productQuantities", productQuantities);
            intent.putIntegerArrayListExtra("productTotals", productTotals);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid total amount", Toast.LENGTH_SHORT).show();
        }
    }
}
