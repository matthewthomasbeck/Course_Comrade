/********************************************************************************/
/* This code and its associated files were created at the instruction of        */
/* professors at the University of Texas at San Antonio during my time as a     */
/* student at the university. I, Matthew Thomas Beck, can confirm that myself   */
/* and any project partners (if applicable) were the ones responsible for       */
/* writing it.                                                                  */
/********************************************************************************/





/************************************************************/
/*************** IMPORT / CREATE DEPENDENCIES ***************/
/************************************************************/


/********** IMPORT DEPENDENCIES **********/

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursecomrade.R
import java.io.BufferedReader
import java.io.InputStreamReader
import androidx.appcompat.widget.Toolbar
import com.example.coursecomrade.ClassAdapter





/*******************************************************/
/*************** ClassesActivity.kt JAVA ***************/
/*******************************************************/


/********** CLASSES ACTIVITY CLASS **********/

class ClassesActivity : AppCompatActivity() {

    /***** set variables *****/

    private lateinit var classesRecyclerView: RecyclerView
    private lateinit var buttonProfile: ImageButton
    private lateinit var toolbarTitle: TextView
    private val classList = mutableListOf<ClassData>()


    /********** CREATE PAGE **********/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classes)

        // Toolbar setup
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = getDrawable(R.drawable.baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Profile button and title
        buttonProfile = findViewById(R.id.buttonProfile)
        toolbarTitle = toolbar.findViewById(R.id.toolbarTitle)
        toolbarTitle.text = "Classes"
        buttonProfile.setOnClickListener { showProfileOptions(it) }

        // RecyclerView setup
        classesRecyclerView = findViewById(R.id.classesRecyclerView)
        classesRecyclerView.layoutManager = LinearLayoutManager(this)

        loadClasses()
        classesRecyclerView.adapter = ClassAdapter(this, classList)
    }

    /********** PROFILE TAB **********/

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

    private fun loadClasses() {
        val inputStream = assets.open("classes.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.readLine() // Skip header
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            val tokens = line!!.split(",").map { it.trim() }
            if (tokens.size >= 4) {
                classList.add(
                    ClassData(
                        tokens[0],
                        tokens[1],
                        tokens[2],
                        tokens[5]
                    )
                )
            }
        }
        reader.close()
    }
}
