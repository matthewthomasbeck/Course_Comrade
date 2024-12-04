package com.example.coursecomrade;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileInfo;
    private EditText oldPasswordInput;
    private EditText newPasswordInput;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileInfo = findViewById(R.id.profileInfo);
        oldPasswordInput = findViewById(R.id.oldPasswordInput);
        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmButton = findViewById(R.id.confirmButton);

        // Get the logged-in username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("logged_in_user", "Unknown");

        profileInfo.setText("Hello, " + username);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = oldPasswordInput.getText().toString().trim();
                String newPassword = newPasswordInput.getText().toString().trim();

                if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (changePassword(username, oldPassword, newPassword)) {
                    Toast.makeText(ProfileActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean changePassword(String username, String oldPassword, String newPassword) {
        File file = new File(getFilesDir(), "users.csv");
        if (!file.exists()) {
            Log.e("ProfileActivity", "Users file not found");
            return false;
        }

        try {
            // Read file and update password
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String header = reader.readLine(); // Read the header
            lines.add(header); // Preserve the header line

            String line;
            boolean passwordChanged = false;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 2 && tokens[0].trim().equals(username)) {
                    if (tokens[1].trim().equals(oldPassword)) {
                        lines.add(username + "," + newPassword);
                        passwordChanged = true;
                    } else {
                        lines.add(line);
                    }
                } else {
                    lines.add(line);
                }
            }
            reader.close();

            if (passwordChanged) {
                // Write updated lines back to the file
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (String updatedLine : lines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
                writer.close();
                return true;
            }

        } catch (Exception e) {
            Log.e("ProfileActivity", "Error changing password: " + e.getMessage());
        }

        return false;
    }
}
