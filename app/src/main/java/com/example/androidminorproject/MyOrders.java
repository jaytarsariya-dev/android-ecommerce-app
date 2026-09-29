package com.example.androidminorproject;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyOrders extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> ordersList;
    private Button clearOrdersButton, generateBillButton;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 101;

    private static final String PREF_ORDERS = "MyOrders";
    private static final String ORDERS_LIST_KEY = "ordersList";
    private static final String LAST_ORDER_ID_KEY = "lastOrderId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        recyclerView = findViewById(R.id.recyclerView);
        clearOrdersButton = findViewById(R.id.clearorder);
        generateBillButton = findViewById(R.id.generateBill);

        // Retrieve orders
        ordersList = getOrders();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordersAdapter = new OrdersAdapter(ordersList);
        recyclerView.setAdapter(ordersAdapter);

        if (ordersList.isEmpty()) {
            Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
        }

        // Clear Orders button listener
        clearOrdersButton.setOnClickListener(v -> clearOrders());

        // Generate Bill button listener
        generateBillButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                generatePDFBill();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST_CODE);
            }
        });
    }

    private List<Order> getOrders() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_ORDERS, MODE_PRIVATE);
        String ordersJson = sharedPreferences.getString(ORDERS_LIST_KEY, "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<List<Order>>() {}.getType();
        List<Order> allOrders = gson.fromJson(ordersJson, type);

        return allOrders != null ? allOrders : new ArrayList<>();
    }

    private void clearOrders() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_ORDERS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear order list and reset Order ID
        editor.putString(ORDERS_LIST_KEY, "[]");
        editor.putInt(LAST_ORDER_ID_KEY, 0);
        editor.apply();

        Toast.makeText(this, "All orders cleared", Toast.LENGTH_SHORT).show();

        // Refresh RecyclerView
        ordersList.clear();
        ordersAdapter.notifyDataSetChanged();
    }

    private void generatePDFBill() {
        if (ordersList.isEmpty()) {
            Toast.makeText(this, "No orders available to generate a bill", Toast.LENGTH_SHORT).show();
            return;
        }

        // Define the file path
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(directoryPath, "Order_Bill.pdf");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Add Title
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph("Order Bill", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add Date
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            document.add(new Paragraph("Date: " + currentDate));

            // Create Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell(getBoldCell("Product Name"));
            table.addCell(getBoldCell("Quantity"));
            table.addCell(getBoldCell("Price"));
            table.addCell(getBoldCell("UTR Number"));

            // Add Order Details
            for (Order order : ordersList) {
                List<String> productNames = order.getProductNames();
                List<Integer> productQuantities = order.getProductQuantities();
                List<Integer> productTotals = order.getProductTotals();

                for (int i = 0; i < productNames.size(); i++) {
                    table.addCell(new PdfPCell(new Phrase(productNames.get(i))));
                    table.addCell(new PdfPCell(new Phrase(productQuantities.get(i))));
                    table.addCell(new PdfPCell(new Phrase(productTotals.get(i))));
                    table.addCell(new PdfPCell(new Phrase(order.getUtrNumber())));
                }
            }

            document.add(table);
            document.close();

            Toast.makeText(this, "Bill saved in Downloads as Order_Bill.pdf", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating bill", Toast.LENGTH_SHORT).show();
        }
    }

    private PdfPCell getBoldCell(String text) {
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(text, boldFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyOrders.this, User.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePDFBill();
            } else {
                Toast.makeText(this, "Storage permission is required to generate bill", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
