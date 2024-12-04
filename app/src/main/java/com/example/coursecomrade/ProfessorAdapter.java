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

public class ProfessorAdapter extends RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder> {

    private final Context context;
    private final List<ProfessorData> professorList;

    // Constructor
    public ProfessorAdapter(Context context, List<ProfessorData> professorList) {
        this.context = context;
        this.professorList = professorList;
    }

    // ViewHolder class
    public static class ProfessorViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView rating;

        public ProfessorViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.professorName);
            rating = itemView.findViewById(R.id.professorRating);
        }
    }

    @NonNull
    @Override
    public ProfessorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.professor_item, parent, false);
        return new ProfessorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessorViewHolder holder, int position) {
        ProfessorData professor = professorList.get(position);

        // Set professor name
        holder.name.setText(professor.getFirstName() + " " + professor.getLastName());

        // Check if the rating is 0
        if ("0".equals(professor.getRating())) {
            holder.rating.setText("Not yet rated");
        } else {
            holder.rating.setText(professor.getRating() + " Stars");
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

        // Handle click to open ProfessorDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfessorDetailActivity.class);
            intent.putExtra("PROFESSOR_ID", professor.getProfessorId());
            intent.putExtra("PROFESSOR_NAME", professor.getFirstName() + " " + professor.getLastName());
            intent.putExtra("PROFESSOR_EMAIL", professor.getEmail());
            intent.putExtra("PROFESSOR_RATING", "0".equals(professor.getRating()) ? "No Rating Available" : professor.getRating());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return professorList.size();
    }
}
