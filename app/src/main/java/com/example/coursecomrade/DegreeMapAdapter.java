package com.example.coursecomrade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class DegreeMapAdapter extends RecyclerView.Adapter<DegreeMapAdapter.ViewHolder> {

    private final Context context;
    private final List<String> semesterList;

    public DegreeMapAdapter(Context context, List<String> semesterList) {
        this.context = context;
        this.semesterList = semesterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.semester_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String semesterName = semesterList.get(position);

        // Set semester name
        holder.semesterTitle.setText(semesterName);

        // Calculate total credit hours for the semester
        int totalCreditHours = calculateCreditHours(semesterName);
        holder.semesterHours.setText("Hours: " + totalCreditHours);

        // Set alternating background colors
        int backgroundColor;
        switch (position % 3) {
            case 0:
                backgroundColor = R.color.tan_shade_1;
                break;
            case 1:
                backgroundColor = R.color.tan_shade_2;
                break;
            default:
                backgroundColor = R.color.tan_shade_3;
                break;
        }
        holder.itemView.setBackgroundResource(backgroundColor);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SemesterDetailActivity.class);
            intent.putExtra("SEMESTER_NAME", semesterName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return semesterList.size();
    }

    private int calculateCreditHours(String semesterName) {
        int totalCreditHours = 0;

        try {
            // Get sanitized file name for the semester
            String sanitizedSemesterName = semesterName.replace(" ", "_").replace("-", "").toLowerCase();
            String username = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getString("logged_in_user", "default_user");
            File semesterFile = new File(context.getFilesDir(), username + "_" + sanitizedSemesterName + ".csv");

            if (semesterFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(semesterFile))) {
                    reader.readLine(); // Skip header
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Each class in the semester contributes 3 credit hours
                        totalCreditHours += 3;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalCreditHours;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView semesterTitle;
        TextView semesterHours;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            semesterTitle = itemView.findViewById(R.id.semesterName);
            semesterHours = itemView.findViewById(R.id.semesterHours);
        }
    }
}
