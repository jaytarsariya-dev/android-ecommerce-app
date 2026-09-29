package com.example.androidminorproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {

    private EditText emailEditText, mobileEditText, localityEditText, addressEditText;
    private Button continueToPaymentButton;
    private int cartTotal;
    private ArrayList<String> productNames;
    private ArrayList<Integer> productQuantities, productTotals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        localityEditText = findViewById(R.id.localityEditText);
        addressEditText = findViewById(R.id.addressEditText);
        continueToPaymentButton = findViewById(R.id.continueToPaymentButton);

        // Retrieve data from the previous intent
        cartTotal = getIntent().getIntExtra("cartTotal", 0);
        productNames = getIntent().getStringArrayListExtra("productNames");
        productQuantities = getIntent().getIntegerArrayListExtra("productQuantities");
        productTotals = getIntent().getIntegerArrayListExtra("productTotals");

        // Button Click Listener
        continueToPaymentButton.setOnClickListener(v -> {
            if (isValidInput()) {
                // Pass data to PaymentActivity
                Intent intent = new Intent(OrderDetailsActivity.this, PaymentActivity.class);
                intent.putExtra("cartTotal", cartTotal);
                intent.putStringArrayListExtra("productNames", productNames);
                intent.putIntegerArrayListExtra("productQuantities", productQuantities);
                intent.putIntegerArrayListExtra("productTotals", productTotals);
                intent.putExtra("email", emailEditText.getText().toString().trim());
                intent.putExtra("mobile", mobileEditText.getText().toString().trim());
                intent.putExtra("locality", localityEditText.getText().toString().trim());
                intent.putExtra("address", addressEditText.getText().toString().trim());

                startActivity(intent);
            } else {
                Toast.makeText(this, "Please fill all details with valid input", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Input Validation Method
    private boolean isValidInput() {
        boolean isValid = true;

        if (emailEditText.getText().toString().trim().isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString().trim()).matches()) {
            emailEditText.setError("Enter a valid email");
            isValid = false;
        }

        if (mobileEditText.getText().toString().trim().isEmpty() ||
                mobileEditText.getText().toString().trim().length() != 10 ||
                !mobileEditText.getText().toString().matches("\\d+")) {
            mobileEditText.setError("Enter a valid 10-digit mobile number");
            isValid = false;
        }

        if (localityEditText.getText().toString().trim().isEmpty()) {
            localityEditText.setError("Enter locality");
            isValid = false;
        }

        if (addressEditText.getText().toString().trim().isEmpty()) {
            addressEditText.setError("Enter address");
            isValid = false;
        }

        return isValid;
    }
}
