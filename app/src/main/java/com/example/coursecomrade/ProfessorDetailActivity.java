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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessorDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_detail);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(getDrawable(R.drawable.baseline_arrow_back_24));
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        // Set toolbar title
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Professor Details");

        // Set up profile button
        ImageButton buttonProfile = findViewById(R.id.buttonProfile);
        buttonProfile.setOnClickListener(this::showProfileOptions);

        // Set professor name
        TextView nameText = findViewById(R.id.professorName);
        String professorName = getIntent().getStringExtra("PROFESSOR_NAME");
        nameText.setText(professorName != null ? professorName : "No name available");

        // Set professor email
        TextView emailText = findViewById(R.id.professorEmail);
        String professorEmail = getIntent().getStringExtra("PROFESSOR_EMAIL");
        emailText.setText(professorEmail != null ? professorEmail : "No email available");

        // Set professor rating
        TextView ratingText = findViewById(R.id.professorRating);
        String rating = getIntent().getStringExtra("PROFESSOR_RATING");
        if (rating == null || rating.trim().equals("0") || rating.trim().equals("N/A")) {
            ratingText.setText("No Rating Available");
        } else {
            ratingText.setText(rating.trim() + " Stars");
        }

        // Get professor ID from intent
        String professorId = getIntent().getStringExtra("PROFESSOR_ID");
        if (professorId == null) return;

        // Load classes taught by the professor
        List<ClassData> classes = getClassesForProfessor(professorId);
        Log.d("ProfessorClasses", "Classes taught by professor: " + classes);

        // Set up RecyclerView with filtered classes
        RecyclerView recyclerView = findViewById(R.id.classesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ClassAdapter(this, classes));
    }

    private Map<String, ClassData> loadAllClasses() {
        Map<String, ClassData> classMap = new HashMap<>();

        try {
            // Open classes.csv from assets
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("classes.csv")));

            // Skip header
            reader.readLine();

            // Read each line and create a map of classes by ID
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 4) {
                    ClassData classData = new ClassData(
                            tokens[0].trim(),
                            tokens[1].trim(),
                            tokens[2].trim(),
                            tokens[5].trim()
                    );
                    classMap.put(classData.getClassId(), classData);
                }
            }
            reader.close();
        } catch (Exception e) {
            Log.e("CSVError", "Error loading classes: " + e.getMessage());
        }

        return classMap;
    }

    private List<ClassData> getClassesForProfessor(String professorId) {
        List<ClassData> classes = new ArrayList<>();

        try {
            // Load all classes
            Map<String, ClassData> allClasses = loadAllClasses();

            // Open id.csv to map professors to classes
            BufferedReader readerMappings = new BufferedReader(new InputStreamReader(getAssets().open("id.csv")));

            // Skip header
            readerMappings.readLine();

            // Filter classes associated with the professor
            String line;
            while ((line = readerMappings.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 2 && tokens[1].trim().equals(professorId)) {
                    ClassData classData = allClasses.get(tokens[0].trim());
                    if (classData != null) {
                        classes.add(classData);
                    }
                }
            }
            readerMappings.close();

        } catch (Exception e) {
            Log.e("CSVError", "Error getting classes for professor: " + e.getMessage());
        }

        return classes;
    }

    // Function to display profile options menu
    private void showProfileOptions(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_profile) {
                startActivity(new Intent(ProfessorDetailActivity.this, ProfileActivity.class));
                return true;
            } else if (item.getItemId() == R.id.menu_logout) {
                Toast.makeText(ProfessorDetailActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }
}
