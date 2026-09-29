package com.example.androidminorproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class AllCategories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);

        LinearLayout categoryGrocery = findViewById(R.id.categoryGrocery);
        LinearLayout categoryClothing = findViewById(R.id.categoryClothing);
        LinearLayout categoryHousehold = findViewById(R.id.categoryHousehold);
        LinearLayout categoryMobiles = findViewById(R.id.categoryMobiles);
        LinearLayout categoryAppliance = findViewById(R.id.categoryAppliance);

        categoryGrocery.setOnClickListener(v -> openCategoryPage("Grocery"));
        categoryClothing.setOnClickListener(v -> openCategoryPage("Clothing"));
        categoryHousehold.setOnClickListener(v -> openCategoryPage("Household"));
        categoryMobiles.setOnClickListener(v -> openCategoryPage("Mobiles"));
        categoryAppliance.setOnClickListener(v -> openCategoryPage("Appliances"));
    }

    private void openCategoryPage(String category) {
        Intent intent = new Intent(AllCategories.this, CategoryRecords.class);
        intent.putExtra("CATEGORY_NAME", category);
        startActivity(intent);
    }
}
