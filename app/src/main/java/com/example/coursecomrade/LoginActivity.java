package com.example.coursecomrade;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameInput = findViewById(R.id.editTextUsername);
        EditText passwordInput = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.buttonLogin);
        Button createAccountButton = findViewById(R.id.buttonCreateAccount);

        // Ensure `users.csv` exists in filesDir
        initializeUsersFile();

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            loginUser(username, password);
        });

        createAccountButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (createAccount(username, password)) {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeUsersFile() {
        File file = new File(getFilesDir(), "users.csv");
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("username,password\n");
                Log.d("FileInit", "users.csv created in filesDir");
            } catch (IOException e) {
                Log.e("FileError", "Error initializing users.csv: " + e.getMessage());
            }
        }
    }

    private boolean checkCredentials(String username, String password) {
        File file = new File(getFilesDir(), "users.csv");
        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Skip the header line
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 2 && tokens[0].trim().equals(username) && tokens[1].trim().equals(password)) {
                    // Save username in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("is_logged_in", true);
                    editor.putString("logged_in_user", username);
                    editor.apply();
                    return true;
                }
            }
        } catch (IOException e) {
            Log.e("CSVError", "Error checking credentials: " + e.getMessage());
        }
        return false;
    }

    private boolean createAccount(String username, String password) {
        File file = new File(getFilesDir(), "users.csv");
        if (!file.exists()) {
            initializeUsersFile();
        }

        try {
            // Read existing users to check if username already exists
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine(); // Skip the header
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens[0].trim().equals(username)) {
                    reader.close();
                    return false; // Username already exists
                }
            }
            reader.close();

            // Append new account
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(username + "," + password + "\n");
            }
        } catch (IOException e) {
            Log.e("AccountError", "Error creating account: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void loginUser(String username, String password) {
        if (checkCredentials(username, password)) {
            // Save login state in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("is_logged_in", true);
            editor.apply();

            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_logged_in", false);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
