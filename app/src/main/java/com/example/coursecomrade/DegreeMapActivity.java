package com.example.coursecomrade;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class DegreeMapActivity extends AppCompatActivity {

    /***** Set variables *****/
    private RecyclerView semesterRecyclerView;
    private ImageButton buttonProfile;
    private TextView toolbarTitle;
    private final List<String> semesterList = new ArrayList<>();
    private DegreeMapAdapter degreeMapAdapter;

    /********** CREATE PAGE **********/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_degree_map);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setNavigationIcon(getDrawable(R.drawable.baseline_arrow_back_24));
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        // Profile button and title
        buttonProfile = findViewById(R.id.buttonProfile);
        toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Degree Map");
        buttonProfile.setOnClickListener(this::showProfileOptions);

        // RecyclerView setup
        semesterRecyclerView = findViewById(R.id.semesterRecyclerView);
        semesterRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load default semesters
        loadSemesters();
        degreeMapAdapter = new DegreeMapAdapter(this, semesterList);
        semesterRecyclerView.setAdapter(degreeMapAdapter);
    }

    /********** PROFILE TAB **********/
    private void showProfileOptions(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_profile) {
                    toolbarTitle.setText("Profile");
                    return true;
                } else if (item.getItemId() == R.id.menu_logout) {
                    Toast.makeText(DegreeMapActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();
    }

    /********** LOAD SEMESTERS **********/
    private void loadSemesters() {
        semesterList.add("Semester 1 - Autumn");
        semesterList.add("Semester 2 - Spring");
        semesterList.add("Semester 2.5 - Summer");
        semesterList.add("Semester 3 - Autumn");
        semesterList.add("Semester 4 - Spring");
        semesterList.add("Semester 4.5 - Summer");
        semesterList.add("Semester 5 - Autumn");
        semesterList.add("Semester 6 - Spring");
        semesterList.add("Semester 6.5 - Summer");
        semesterList.add("Semester 7 - Autumn");
        semesterList.add("Semester 8 - Spring");
    }

    /********** ADD NEW SEMESTER **********/
    private void addSemester() {
        int newSemesterNumber = semesterList.size(); // Determine new semester number
        String newSemester = "Semester " + newSemesterNumber + " - New";
        semesterList.add(semesterList.size() - 1, newSemester); // Add before "Add Semester"
        degreeMapAdapter.notifyItemInserted(semesterList.size() - 2); // Update RecyclerView
    }
}