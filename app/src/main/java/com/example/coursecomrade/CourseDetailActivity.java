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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setNavigationIcon(getDrawable(R.drawable.baseline_arrow_back_24));
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        // Set toolbar title
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Course Details");

        // Set up profile button
        ImageButton buttonProfile = findViewById(R.id.buttonProfile);
        buttonProfile.setOnClickListener(this::showProfileOptions);

        // Get data from intent
        String courseId = getIntent().getStringExtra("COURSE_ID");
        if (courseId == null) return;

        String courseName = getIntent().getStringExtra("COURSE_NAME");
        if (courseName == null) courseName = "Unknown Course";

        String courseDescription = getIntent().getStringExtra("COURSE_DESCRIPTION");
        if (courseDescription == null) courseDescription = "No description available.";

        // Display course details
        TextView courseNameText = findViewById(R.id.courseName);
        courseNameText.setText(courseName);

        TextView descriptionText = findViewById(R.id.courseDescription);
        descriptionText.setText(courseDescription);

        // Load and display professors associated with the course
        List<ProfessorData> professors = getProfessorsForCourse(courseId);
        Log.d("FilteredProfessors", "Professors for course: " + professors);

        // Set up RecyclerView with filtered professors
        RecyclerView recyclerView = findViewById(R.id.professorsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ProfessorAdapter(this, professors));
    }

    private Map<String, ProfessorData> loadAllProfessors() {
        Map<String, ProfessorData> professorMap = new HashMap<>();

        try {
            // Open professors.csv from assets
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("professors.csv")));

            // Skip header
            reader.readLine();

            // Read each line and create a map of professors by ID
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
                    professorMap.put(professor.getProfessorId(), professor);
                }
            }
            reader.close();
        } catch (Exception e) {
            Log.e("CSVError", "Error loading professors: " + e.getMessage());
        }

        return professorMap;
    }

    private List<ProfessorData> getProfessorsForCourse(String classId) {
        List<ProfessorData> professors = new ArrayList<>();

        try {
            // Load all professors
            Map<String, ProfessorData> allProfessors = loadAllProfessors();

            // Open id.csv to map professors to classes
            BufferedReader readerMappings = new BufferedReader(new InputStreamReader(getAssets().open("id.csv")));

            // Skip header
            readerMappings.readLine();

            // Filter professors associated with the course
            String line;
            while ((line = readerMappings.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 2 && tokens[0].trim().equals(classId)) {
                    ProfessorData professor = allProfessors.get(tokens[1].trim());
                    if (professor != null) {
                        professors.add(professor);
                    }
                }
            }
            readerMappings.close();

        } catch (Exception e) {
            Log.e("CSVError", "Error getting professors for course: " + e.getMessage());
        }

        return professors;
    }

    // Function to display profile options menu
    private void showProfileOptions(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_profile) {
                startActivity(new Intent(CourseDetailActivity.this, ProfileActivity.class));
                return true;
            } else if (item.getItemId() == R.id.menu_logout) {
                Toast.makeText(CourseDetailActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }
}
