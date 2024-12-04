package com.example.coursecomrade;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProfessorsActivity extends AppCompatActivity {

    private ImageButton buttonProfile;
    private TextView toolbarTitle;
    private RecyclerView professorsRecyclerView;
    private final List<ProfessorData> professorList = new ArrayList<>();
    private ProfessorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professors);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setNavigationIcon(getDrawable(R.drawable.baseline_arrow_back_24));
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        // Set up profile button and toolbar title
        buttonProfile = findViewById(R.id.buttonProfile);
        toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Professors");
        buttonProfile.setOnClickListener(this::showProfileOptions);

        // Set up RecyclerView
        professorsRecyclerView = findViewById(R.id.professorsRecyclerView);
        professorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with an empty list and attach it to RecyclerView
        adapter = new ProfessorAdapter(this, professorList);
        professorsRecyclerView.setAdapter(adapter);

        // Load professor data from CSV
        loadProfessors();
    }

    /********** PROFILE TAB **********/
    private void showProfileOptions(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_profile) {
                toolbarTitle.setText("Profile");
                Intent intent = new Intent(ProfessorsActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.menu_logout) {
                logoutUser();
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }

    /********** LOGOUT FUNCTION **********/
    private void logoutUser() {
        // Clear the login state
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("is_logged_in", false)
                .apply();

        // Show a Toast message
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Finish ProfessorsActivity to prevent the user from navigating back
    }

    /********** LOAD PROFESSORS FROM CSV **********/
    private void loadProfessors() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("professors.csv")))) {
            reader.readLine(); // Skip header
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 5) {
                    ProfessorData professor = new ProfessorData(
                            tokens[0].trim(),
                            tokens[1].trim(),
                            tokens[2].trim(),
                            tokens[3].trim(),
                            tokens[4].trim()
                    );
                    professorList.add(professor);
                    Log.d("ProfessorData", professor.toString());
                }
            }
        } catch (Exception e) {
            Log.e("CSVError", "Error loading professors: " + e.getMessage());
        }
    }
}
