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
import com.example.coursecomrade.R
import java.io.BufferedReader
import java.io.InputStreamReader

class ProfessorsActivity : AppCompatActivity() {

    private lateinit var buttonProfile: ImageButton
    private lateinit var toolbarTitle: TextView
    private lateinit var professorsRecyclerView: RecyclerView
    private val professorList = mutableListOf<ProfessorData>()
    private lateinit var adapter: ProfessorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professors)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = getDrawable(R.drawable.baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Set up profile button and toolbar title
        buttonProfile = findViewById(R.id.buttonProfile)
        toolbarTitle = toolbar.findViewById(R.id.toolbarTitle)
        toolbarTitle.text = "Professors"
        buttonProfile.setOnClickListener { showProfileOptions(it) }

        // Set up RecyclerView
        professorsRecyclerView = findViewById(R.id.professorsRecyclerView)
        professorsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with an empty list and attach it to RecyclerView
        adapter = ProfessorAdapter(this, professorList)
        professorsRecyclerView.adapter = adapter

        // Load professor data from CSV
        loadProfessors()
    }

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

    private fun loadProfessors() {
        try {
            val inputStream = assets.open("professors.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.readLine() // Skip header
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
                    professorList.add(professor)
                    android.util.Log.d("ProfessorData", professor.toString())
                }
            }
            reader.close()
        } catch (e: Exception) {
            android.util.Log.e("CSVError", "Error loading professors: ${e.message}")
        }
    }
}
