package com.example.coursecomrade

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coursecomrade.R

class ProfessorAdapter(
    private val context: Context,
    private val professorList: List<ProfessorData>
) : RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder>() {

    class ProfessorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.professorName)
        val rating: TextView = itemView.findViewById(R.id.professorRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.professor_item, parent, false)
        return ProfessorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfessorViewHolder, position: Int) {
        val professor = professorList[position]

        // Set professor name
        holder.name.text = "${professor.firstName} ${professor.lastName}"

        // Check if the rating is 0
        holder.rating.text = if (professor.rating == "0") {
            "Not yet rated"
        } else {
            "${professor.rating} Stars"
        }

        // Set alternating background colors
        val backgroundColor = when (position % 3) {
            0 -> R.color.tan_shade_1
            1 -> R.color.tan_shade_2
            else -> R.color.tan_shade_3
        }
        holder.itemView.setBackgroundResource(backgroundColor)

        // Handle click to open ProfessorDetailActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProfessorDetailActivity::class.java)
            intent.putExtra("PROFESSOR_ID", professor.professorId)
            intent.putExtra("PROFESSOR_NAME", "${professor.firstName} ${professor.lastName}")
            intent.putExtra("PROFESSOR_EMAIL", professor.email)
            intent.putExtra("PROFESSOR_RATING", if (professor.rating == "0") "No Rating Available" else professor.rating)
            context.startActivity(intent)
        }
    }


    override fun getItemCount() = professorList.size
}
