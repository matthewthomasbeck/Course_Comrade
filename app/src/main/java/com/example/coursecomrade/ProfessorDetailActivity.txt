package com.example.coursecomrade

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

class ProfessorDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professor_detail)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = getDrawable(R.drawable.baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Set toolbar title
        val toolbarTitle: TextView = findViewById(R.id.toolbarTitle)
        toolbarTitle.text = "Professor Details"

        // Set up profile button
        val buttonProfile: ImageButton = findViewById(R.id.buttonProfile)
        buttonProfile.setOnClickListener { showProfileOptions(it) }

        // Set professor name
        val nameText: TextView = findViewById(R.id.professorName)
        nameText.text = intent.getStringExtra("PROFESSOR_NAME") ?: "No name available"

        // Set professor email
        val emailText: TextView = findViewById(R.id.professorEmail)
        emailText.text = intent.getStringExtra("PROFESSOR_EMAIL") ?: "No email available"

        // Set professor rating
        val ratingText: TextView = findViewById(R.id.professorRating)
        val rating = intent.getStringExtra("PROFESSOR_RATING")?.trim() ?: "N/A"
        ratingText.text = when {
            rating == "0" -> "No Rating Available"
            rating == "N/A" -> "No Rating Available"
            else -> "$rating Stars"
        }


        // Get professor ID from intent
        val professorId = intent.getStringExtra("PROFESSOR_ID") ?: return

        // Load classes taught by the professor
        val classes = getClassesForProfessor(professorId)
        Log.d("ProfessorClasses", "Classes taught by professor: $classes")

        // Set up RecyclerView with filtered classes
        val recyclerView: RecyclerView = findViewById(R.id.classesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ClassAdapter(this, classes)
    }

    private fun loadAllClasses(): Map<String, ClassData> {
        val classMap = mutableMapOf<String, ClassData>()

        try {
            // Open classes.csv from assets
            val inputStream = assets.open("classes.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            // Skip header
            reader.readLine()

            // Read each line and create a map of classes by ID
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val tokens = line!!.split(",").map { it.trim() }
                if (tokens.size >= 4) {
                    val classData = ClassData(
                        classId = tokens[0],
                        className = tokens[1],
                        classCode = tokens[2],
                        description = tokens[5]
                    )
                    classMap[classData.classId] = classData
                }
            }
            reader.close()
        } catch (e: Exception) {
            Log.e("CSVError", "Error loading classes: ${e.message}")
        }

        return classMap
    }

    private fun getClassesForProfessor(professorId: String): List<ClassData> {
        val classes = mutableListOf<ClassData>()

        try {
            // Load all classes
            val allClasses = loadAllClasses()

            // Open id.csv to map professors to classes
            val inputStreamMappings = assets.open("id.csv")
            val readerMappings = BufferedReader(InputStreamReader(inputStreamMappings))

            // Skip header
            readerMappings.readLine()

            // Filter classes associated with the professor
            var line: String?
            while (readerMappings.readLine().also { line = it } != null) {
                val tokens = line!!.split(",").map { it.trim() }
                if (tokens.size == 2 && tokens[1] == professorId) {
                    val classData = allClasses[tokens[0]]
                    if (classData != null) {
                        classes.add(classData)
                    }
                }
            }
            readerMappings.close()

        } catch (e: Exception) {
            Log.e("CSVError", "Error getting classes for professor: ${e.message}")
        }

        return classes
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
