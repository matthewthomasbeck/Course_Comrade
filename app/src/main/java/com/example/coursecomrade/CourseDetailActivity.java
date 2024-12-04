package com.example.coursecomrade;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
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

        // Add Class to Semester Button
        Button addClassButton = findViewById(R.id.buttonAddClassToSemester);
        addClassButton.setOnClickListener(v -> showAddClassDialog());
    }

    private void showAddClassDialog() {
        String[] semesters = {
                "Semester 1 - Autumn",
                "Semester 2 - Spring",
                "Semester 2.5 - Summer",
                "Semester 3 - Autumn",
                "Semester 4 - Spring",
                "Semester 4.5 - Summer",
                "Semester 5 - Autumn",
                "Semester 6 - Spring",
                "Semester 6.5 - Summer",
                "Semester 7 - Autumn",
                "Semester 8 - Spring"
        };

        // Create a dialog for semester selection
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select Semester")
                .setItems(semesters, (dialog, which) -> {
                    String selectedSemester = semesters[which];
                    addClassToSemester(selectedSemester);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void addClassToSemester(String semester) {
        String classId = getIntent().getStringExtra("COURSE_ID");
        String className = getIntent().getStringExtra("COURSE_NAME");
        String classCode = getIntent().getStringExtra("COURSE_CODE");
        String creditHours = "3"; // You can replace this with actual data if available

        if (classId == null || className == null || classCode == null) {
            Toast.makeText(this, "Error: Missing class details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sanitize semester name for file naming
        String sanitizedSemesterName = semester.replace(" ", "_").replace("-", "").toLowerCase();
        String username = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("logged_in_user", "default_user");
        File semesterFile = new File(getFilesDir(), username + "_" + sanitizedSemesterName + ".csv");

        try (FileWriter writer = new FileWriter(semesterFile, true)) {
            writer.write(classId + "," + className + "," + classCode + "," + creditHours + "\n");
            Toast.makeText(this, "Class added to " + semester, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("FileError", "Error adding class to semester: " + e.getMessage());
            Toast.makeText(this, "Error saving class to semester", Toast.LENGTH_SHORT).show();
        }
    }

    private Map<String, ProfessorData> loadAllProfessors() {
        Map<String, ProfessorData> professorMap = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("professors.csv")));
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
            Map<String, ProfessorData> allProfessors = loadAllProfessors();
            BufferedReader readerMappings = new BufferedReader(new InputStreamReader(getAssets().open("id.csv")));
            readerMappings.readLine(); // Skip header
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
