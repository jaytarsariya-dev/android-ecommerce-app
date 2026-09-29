package com.example.androidminorproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androidminorproject.databinding.ActivityAboutusBinding;

import java.util.ArrayList;
import java.util.List;

public class Aboutus extends AppCompatActivity {

    private ActivityAboutusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup team members RecyclerView
        List<TeamMember> teamMembers = new ArrayList<>();
        teamMembers.add(new TeamMember("Vikash Yadav", "CEO", R.drawable.vikash));
        teamMembers.add(new TeamMember("Jay Tarsariya", "CTO", R.drawable.jay));
        teamMembers.add(new TeamMember("Riddhi Savaliya", "Lead Developer", R.drawable.riddhi));
        teamMembers.add(new TeamMember("Pavitra Surati", "Marketing Head", R.drawable.pavitra));
        teamMembers.add(new TeamMember("Jeel Sirsath", "Customer Experience Specialist", R.drawable.jeel));

        setupTeamRecyclerView(teamMembers);

        // Handle Contact Us button click
        binding.btnContact.setOnClickListener(v -> {
            Toast.makeText(Aboutus.this, "Contact Us: shopgalaxy908@gmail.com", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Aboutus.this, Contactus.class);
            startActivity(intent);
        });
    }

    private void setupTeamRecyclerView(List<TeamMember> teamMembers) {
        binding.rvTeamMembers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTeamMembers.setAdapter(new TeamAdapter(teamMembers));
    }
}
