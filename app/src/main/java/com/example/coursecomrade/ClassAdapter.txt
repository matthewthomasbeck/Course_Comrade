package com.example.coursecomrade

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coursecomrade.R

class ClassAdapter(
    private val context: Context,
    private val classList: List<ClassData>
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val className: TextView = itemView.findViewById(R.id.className)
        val classCode: TextView = itemView.findViewById(R.id.classCode)
        val secondLine: TextView = itemView.findViewById(R.id.secondLine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.class_item, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classData = classList[position]

        // Set the course name and course number
        holder.className.text = classData.className
        holder.classCode.text = classData.classCode

        // Set the optional second line if available
        holder.secondLine.text = if (classData.className.contains("\n")) {
            classData.className.split("\n")[1]
        } else {
            ""
        }

        // Set alternating background colors
        val backgroundColor = when (position % 3) {
            0 -> R.color.tan_shade_1
            1 -> R.color.tan_shade_2
            else -> R.color.tan_shade_3
        }
        holder.itemView.setBackgroundResource(backgroundColor)

        // Set up item click listener
        holder.itemView.setOnClickListener {
            // Log the full course description
            Log.d(
                "ClassAdapter",
                "Course Description for ${classData.classCode}: ${classData.description}"
            )

            val intent = Intent(context, CourseDetailActivity::class.java)
            intent.putExtra("COURSE_ID", classData.classId)
            intent.putExtra("COURSE_CODE", classData.classCode)
            intent.putExtra("COURSE_NAME", classData.className)
            intent.putExtra("COURSE_DESCRIPTION", classData.description)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = classList.size
}
