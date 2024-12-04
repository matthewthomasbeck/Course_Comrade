package com.example.coursecomrade;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private final Context context;
    private final List<ClassData> classList;

    // Constructor
    public ClassAdapter(Context context, List<ClassData> classList) {
        this.context = context;
        this.classList = classList;
    }

    // ViewHolder class
    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        public TextView className;
        public TextView classCode;
        public TextView secondLine;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.className);
            classCode = itemView.findViewById(R.id.classCode);
            secondLine = itemView.findViewById(R.id.secondLine);
        }
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassData classData = classList.get(position);

        // Set the course name and course number
        holder.className.setText(classData.getClassName());
        holder.classCode.setText(classData.getClassCode());

        // Set the optional second line if available
        if (classData.getClassName().contains("\n")) {
            holder.secondLine.setText(classData.getClassName().split("\n")[1]);
        } else {
            holder.secondLine.setText("");
        }

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

        // Set up item click listener
        holder.itemView.setOnClickListener(v -> {
            // Log the full course description
            Log.d("ClassAdapter", "Course Description for " + classData.getClassCode() + ": " + classData.getDescription());

            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("COURSE_ID", classData.getClassId());
            intent.putExtra("COURSE_CODE", classData.getClassCode());
            intent.putExtra("COURSE_NAME", classData.getClassName());
            intent.putExtra("COURSE_DESCRIPTION", classData.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }
}
