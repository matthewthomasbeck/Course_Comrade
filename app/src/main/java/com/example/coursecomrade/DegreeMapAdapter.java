package com.example.coursecomrade;

import android.content.Context;
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
        String semester = semesterList.get(position);

        // Set semester name
        holder.semesterName.setText(semester);

        // Set hours (default to 0 for now)
        holder.semesterHours.setText("Hours: 0");

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
    }


    @Override
    public int getItemCount() {
        return semesterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView semesterName;
        TextView semesterHours;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            semesterName = itemView.findViewById(R.id.semesterName);
            semesterHours = itemView.findViewById(R.id.semesterHours);
        }
    }
}
