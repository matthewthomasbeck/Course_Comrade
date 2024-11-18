package com.example.coursecomrade

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.InputStreamReader
import com.example.coursecomrade.ProfessorAdapter
import com.example.coursecomrade.R

class CourseDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = getDrawable(R.drawable.baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Set toolbar title
        val toolbarTitle: TextView = findViewById(R.id.toolbarTitle)
        toolbarTitle.text = "Course Details"

        // Set up profile button
        val buttonProfile: ImageButton = findViewById(R.id.buttonProfile)
        buttonProfile.setOnClickListener { showProfileOptions(it) }

        // Get data from intent
        val courseId = intent.getStringExtra("COURSE_ID") ?: return
        val courseName = intent.getStringExtra("COURSE_NAME") ?: "Unknown Course"
        val courseDescription = intent.getStringExtra("COURSE_DESCRIPTION") ?: "No description available."

        // Display course details
        val courseNameText: TextView = findViewById(R.id.courseName)
        courseNameText.text = courseName
        val descriptionText: TextView = findViewById(R.id.courseDescription)
        descriptionText.text = courseDescription

        // Load and display professors associated with the course
        val professors = getProfessorsForCourse(courseId)
        Log.d("FilteredProfessors", "Professors for course: $professors")

        // Set up RecyclerView with filtered professors
        val recyclerView: RecyclerView = findViewById(R.id.professorsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProfessorAdapter(this, professors)
    }

    private fun loadAllProfessors(): Map<String, ProfessorData> {
        val professorMap = mutableMapOf<String, ProfessorData>()

        try {
            // Open professors.csv from assets
            val inputStream = assets.open("professors.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            // Skip header
            reader.readLine()

            // Read each line and create a map of professors by ID
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val tokens = line!!.split(",").map { it.trim() }
                if (tokens.size >= 5) {
                    val professor = ProfessorData(
                        professorId = tokens[0],
                        firstName = tokens[1],
                        lastName = tokens[2],
                        email = tokens[3],
                        rating = tokens[4]
                    )
                    professorMap[professor.professorId] = professor
                }
            }
            reader.close()
        } catch (e: Exception) {
            Log.e("CSVError", "Error loading professors: ${e.message}")
        }

        return professorMap
    }

    private fun getProfessorsForCourse(classId: String): List<ProfessorData> {
        val professors = mutableListOf<ProfessorData>()

        try {
            // Load all professors
            val allProfessors = loadAllProfessors()

            // Open id.csv to map professors to classes
            val inputStreamMappings = assets.open("id.csv")
            val readerMappings = BufferedReader(InputStreamReader(inputStreamMappings))

            // Skip header
            readerMappings.readLine()

            // Filter professors associated with the course
            var line: String?
            while (readerMappings.readLine().also { line = it } != null) {
                val tokens = line!!.split(",").map { it.trim() }
                if (tokens.size == 2 && tokens[0] == classId) {
                    val professor = allProfessors[tokens[1]]
                    if (professor != null) {
                        professors.add(professor)
                    }
                }
            }
            readerMappings.close()

        } catch (e: Exception) {
            Log.e("CSVError", "Error getting professors for course: ${e.message}")
        }

        return professors
    }

    // Function to display profile options menu
    private fun showProfileOptions(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.profile_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.menu_logout -> {
                    Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
}
