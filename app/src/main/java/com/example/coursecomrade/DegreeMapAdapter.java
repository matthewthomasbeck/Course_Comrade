package com.example.coursecomrade;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        // Set alternating background colors
        int backgroundColor;
        switch (position % 3) {
            case 0:
                backgroundColor = R.color.tan_shade_1; // First tan shade
                break;
            case 1:
                backgroundColor = R.color.tan_shade_2; // Second tan shade
                break;
            default:
                backgroundColor = R.color.tan_shade_3; // Third tan shade
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView semesterTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            semesterTitle = itemView.findViewById(R.id.semesterName);
        }
    }
}
