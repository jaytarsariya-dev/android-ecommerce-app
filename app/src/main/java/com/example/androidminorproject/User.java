package com.example.androidminorproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class User extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private MyAdapter1 adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ImageView cart;
    private List<ProductItem> productList = new ArrayList<>(); // Full list of products
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        searchView = findViewById(R.id.search);
        recyclerView = findViewById(R.id.rvProducts);
        cart = findViewById(R.id.ivCart);

        // Initialize Firebase services
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize SharedPreferences (if needed)
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Setup toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup ActionBarDrawerToggle for hamburger menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up navigation menu item selection listener
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            handleMenuItemClick(menuItem);
            drawerLayout.close();
            return true;
        });

        // Navigation Drawer header setup: update username and email
        View headerView = navigationView.getHeaderView(0);
        TextView userEmailTextView = headerView.findViewById(R.id.user_email);

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String displayName = currentUser.getDisplayName();

            // If displayName is not set, you may retrieve it from SharedPreferences as a fallback
            if (displayName == null || displayName.isEmpty()) {
                displayName = sharedPreferences.getString("USER_NAME", "User");
            }
            userEmailTextView.setText(email != null ? email : "No Email");
        } else {
            userEmailTextView.setText("Guest");
        }

        // Setup RecyclerView for grid display
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns grid

        // Load all products initially
        loadAllProducts();

        // Setup SearchView for live search
        setupSearchView();

        // Handle category clicks
        findViewById(R.id.categoryGrocery).setOnClickListener(view -> loadCategoryData("Grocery"));
        findViewById(R.id.categoryClothing).setOnClickListener(view -> loadCategoryData("Clothing"));
        findViewById(R.id.categoryHousehold).setOnClickListener(view -> loadCategoryData("Household"));
        findViewById(R.id.categoryMobiles).setOnClickListener(view -> loadCategoryData("Mobiles"));
        findViewById(R.id.categoryAppliance).setOnClickListener(view -> loadCategoryData("Appliances"));

        // Handle cart icon click
        cart.setOnClickListener(v -> openCartActivity());
    }

    private void openCartActivity() {
        Intent intent = new Intent(User.this, CartActivity.class);
        startActivity(intent);
    }

    private void loadAllProducts() {
        firestore.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList = task.getResult().toObjects(ProductItem.class);
                        if (productList.isEmpty()) {
                            recyclerView.setAdapter(null);
                        } else {
                            setupRecyclerView(productList);
                        }
                    } else {
                        recyclerView.setAdapter(null);
                    }
                });
    }

    private void loadCategoryData(String category) {
        firestore.collection("products")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<ProductItem> filteredList = task.getResult().toObjects(ProductItem.class);
                        if (filteredList.isEmpty()) {
                            recyclerView.setAdapter(null);
                        } else {
                            setupRecyclerView(filteredList);
                        }
                    } else {
                        recyclerView.setAdapter(null);
                    }
                });
    }

    private void setupRecyclerView(List<ProductItem> products) {
        adapter = new MyAdapter1(this, products, productItem -> {
            // Create intent to pass data to DetailActivity1
            Intent intent = new Intent(User.this, DetailActivity1.class);
            intent.putExtra("productName", productItem.getProductName());
            intent.putExtra("productDescription", productItem.getProductDescription());
            intent.putExtra("productPrice", productItem.getProductPrice());
            intent.putExtra("productImage", productItem.getImageUrl());  // Assuming productImage contains the image URL
            intent.putExtra("productCategory", productItem.getCategory());  // Pass the category here
            intent.putExtra("productId", productItem.getProductId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });
    }

    private void filterProducts(String query) {
        List<ProductItem> filteredList = new ArrayList<>();
        for (ProductItem item : productList) {
            if (item.getProductName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        setupRecyclerView(filteredList);
    }

    private void handleMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_orders) {
            startActivity(new Intent(this, MyOrders.class));
        } else if (itemId == R.id.menu_cart) {
            startActivity(new Intent(this, CartActivity.class));
        } else if (itemId == R.id.menu_categories) {
            startActivity(new Intent(this, AllCategories.class));
        } else if (itemId == R.id.menu_wishlist) {
            startActivity(new Intent(this, Wishlist.class));
        } else if (itemId == R.id.menu_aboutus) {
            startActivity(new Intent(this, Aboutus.class));
        } else if (itemId == R.id.menu_contactus) {
            startActivity(new Intent(this, Contactus.class));
        } else if (itemId == R.id.menu_support) {
            startActivity(new Intent(this, Support.class));
        } else if (itemId == R.id.menu_logout) {
            logout();
        }
    }

    private void logout() {
        // Clear the shared preferences for login state
        SharedPreferences sharedPreferences = getSharedPreferences("UserLoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Sign out from FirebaseAuth if needed
        auth.signOut();

        // Redirect user to login activity
        Intent intent = new Intent(User.this, Loginforuser.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}