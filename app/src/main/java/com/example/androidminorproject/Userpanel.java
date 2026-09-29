package com.example.androidminorproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Userpanel extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpanel);

        bottomNavigationView = findViewById(R.id.bot);
        frameLayout = findViewById(R.id.frame_layout);

        // Load Home1Fragment by default when the app starts
        loadFragment(new Home1Fragment(), false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home1) {
                    loadFragment(new Home1Fragment(), false);
                } else if (itemId == R.id.logout) {
                    loadFragment(new LogoutFragment(), false);
                } else if (itemId == R.id.contact) {
                    loadFragment(new ContactusFragment(), false);
                } else if (itemId == R.id.about) {
                    loadFragment(new AboutusFragment(), false);
                }else
                    loadFragment(new cartFragment(), false);
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Simplified logic to always replace the fragment
        fragmentTransaction.replace(R.id.frame_layout, fragment);

        fragmentTransaction.commit();
    }
}
