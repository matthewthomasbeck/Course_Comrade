package com.example.coursecomrade;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SemesterDetailActivity extends AppCompatActivity {

    private List<ClassData> classList = new ArrayList<>();
    private ClassAdapter classAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_detail);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setNavigationIcon(getDrawable(R.drawable.baseline_arrow_back_24));
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        // Set toolbar title
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        String semesterName = getIntent().getStringExtra("SEMESTER_NAME");
        if (semesterName != null) {
            toolbarTitle.setText(semesterName);
        } else {
            toolbarTitle.setText("Semester Details");
        }

        // Set up profile button
        ImageButton buttonProfile = findViewById(R.id.buttonProfile);
        buttonProfile.setOnClickListener(this::showProfileOptions);

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.classesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        classAdapter = new ClassAdapter(this, classList);
        recyclerView.setAdapter(classAdapter);

        // Load classes for the selected semester
        if (semesterName != null) {
            loadClassesForSemester(semesterName);
        } else {
            Toast.makeText(this, "Error: Semester not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadClassesForSemester(String semesterName) {
        String username = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("logged_in_user", "default_user");
        String sanitizedSemesterName = semesterName.replace(" ", "_").replace("-", "").toLowerCase();
        File semesterFile = new File(getFilesDir(), username + "_" + sanitizedSemesterName + ".csv");

        if (!semesterFile.exists()) {
            Toast.makeText(this, "No classes found for " + semesterName, Toast.LENGTH_SHORT).show();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(semesterFile))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 4) {
                    classList.add(new ClassData(
                            tokens[0].trim(), // class_id
                            tokens[1].trim(), // class_name
                            tokens[2].trim(), // class_code
                            tokens[3].trim()  // credit_hours
                    ));
                }
            }
            classAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("FileError", "Error reading semester file: " + e.getMessage());
        }
    }

    private void showProfileOptions(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_profile) {
                startActivity(new Intent(SemesterDetailActivity.this, ProfileActivity.class));
                return true;
            } else if (item.getItemId() == R.id.menu_logout) {
                Toast.makeText(SemesterDetailActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }
}
