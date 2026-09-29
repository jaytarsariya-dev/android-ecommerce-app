package com.example.androidminorproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LogoutFragment extends Fragment {
    Button btn1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        btn1=view.findViewById(R.id.logout);

        btn1.setOnClickListener(v -> showAlertDialog());
        return view;
    }

    private void showAlertDialog() {
        // Create an AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.DialogTheme);

        // Set the title and message
        builder.setTitle("Alert!!")
                .setMessage("Are you sure you want to logout?")

                // Set positive button
                .setPositiveButton("OK", (dialog, which) -> {
                    // Sign out from Firebase
                    FirebaseAuth.getInstance().signOut();

                    // Clear any persistent admin login state (if using SharedPreferences for admin login)
                    SharedPreferences prefs = requireContext().getSharedPreferences("adminLoginPrefs", getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isAdminLoggedIn", false); // Clear the admin login flag
                    editor.apply();

                    // Redirect to Loginforuser activity
                    Intent intent = new Intent(getActivity(), Loginforuser.class);
                    startActivity(intent);
                    getActivity().finish(); // Close the current activity (LogoutFragment)

                })

                // Set negative button
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle the negative button action
                    dialog.dismiss();
                });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
