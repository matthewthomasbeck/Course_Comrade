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

public class MainActivity extends AppCompatActivity {

    /***** Set variables *****/
    private ImageButton buttonProfile;
    private TextView degreeMap;
    private TextView classes;
    private TextView professors;
    private TextView backupButton;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    /********** CREATE PAGE **********/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check login state
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

        if (!isLoggedIn) {
            // Redirect to LoginActivity if not logged in
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Prevent returning to MainActivity without logging in
            return;
        }

        // Load the main layout if the user is logged in
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        buttonProfile = findViewById(R.id.buttonProfile);
        degreeMap = findViewById(R.id.degreeMap);
        classes = findViewById(R.id.classes);
        professors = findViewById(R.id.professors);
        backupButton = findViewById(R.id.backupButton);

        toolbarTitle.setText("Home");

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileOptions(v);
            }
        });

        degreeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DegreeMapActivity.class);
                startActivity(intent);
            }
        });

        classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClassesActivity.class);
                startActivity(intent);
            }
        });

        professors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfessorsActivity.class);
                startActivity(intent);
            }
        });

        backupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement backup functionality
            }
        });

        backupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MeetCreatorsActivity.class);
                startActivity(intent);
            }
        });
    }

    /********** RESUME HOME PAGE **********/
    @Override
    protected void onResume() {
        super.onResume();
        toolbarTitle.setText("Home");
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
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
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

    /********** LOGOUT USER **********/
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
        finish(); // Finish MainActivity to prevent the user from navigating back
    }
}
