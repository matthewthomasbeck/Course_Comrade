/********************************************************************************/
/* This code and its associated files were created at the instruction of        */
/* professors at the University of Texas at San Antonio during my time as a     */
/* student at the university. I, Matthew Thomas Beck, can confirm that myself   */
/* and any project partners (if applicable) were the ones responsible for       */
/* writing it.                                                                  */
/********************************************************************************/

package com.example.coursecomrade;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClassesActivity extends AppCompatActivity {

    /***** Set variables *****/
    private RecyclerView classesRecyclerView;
    private ImageButton buttonProfile;
    private TextView toolbarTitle;
    private final List<ClassData> classList = new ArrayList<>();

    /********** CREATE PAGE **********/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

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
        toolbarTitle.setText("Classes");
        buttonProfile.setOnClickListener(this::showProfileOptions);

        // RecyclerView setup
        classesRecyclerView = findViewById(R.id.classesRecyclerView);
        classesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadClasses();
        classesRecyclerView.setAdapter(new ClassAdapter(this, classList));
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
                    Intent intent = new Intent(ClassesActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.menu_logout) {
                    logoutUser();
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();
    }

    private void logoutUser() {
        // Clear the login state
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_logged_in", false);
        editor.apply();

        // Show a Toast message
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Finish ClassesActivity to prevent the user from navigating back
    }

    private void loadClasses() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("classes.csv")));
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 4) {
                    classList.add(new ClassData(
                            tokens[0].trim(),
                            tokens[1].trim(),
                            tokens[2].trim(),
                            tokens[3].trim()
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
