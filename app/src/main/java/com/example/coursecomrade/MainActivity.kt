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
import androidx.appcompat.widget.Toolbar





/****************************************************/
/*************** MainActivity.kt JAVA ***************/
/****************************************************/


/********** MAIN ACTIVITY CLASS **********/

class MainActivity : AppCompatActivity() {

    /***** set variables *****/

    private lateinit var buttonProfile: ImageButton
    private lateinit var degreeMap: TextView
    private lateinit var classes: TextView
    private lateinit var professors: TextView
    private lateinit var backupButton: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var toolbarTitle: TextView


    /********** CREATE PAGE **********/

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        toolbarTitle = toolbar.findViewById(R.id.toolbarTitle)

        supportActionBar?.hide()

        buttonProfile = findViewById(R.id.buttonProfile)
        degreeMap = findViewById(R.id.degreeMap)
        classes = findViewById(R.id.classes)
        professors = findViewById(R.id.professors)
        backupButton = findViewById(R.id.backupButton)

        toolbarTitle.text = "Home"

        buttonProfile.setOnClickListener {

            showProfileOptions(it)
        }

        degreeMap.setOnClickListener {

            val intent = Intent(this, DegreeMapActivity::class.java)
            startActivity(intent)
        }

        classes.setOnClickListener {

            val intent = Intent(this, ClassesActivity::class.java)
            startActivity(intent)
        }

        professors.setOnClickListener {

            val intent = Intent(this, ProfessorsActivity::class.java)
            startActivity(intent)
        }

        backupButton.setOnClickListener {

            // TODO talk with team if anything needed
        }
    }

    /********** RESUME HOME PAGE **********/

    override fun onResume() {

        super.onResume()
        toolbarTitle.text = "Home"
    }


    /********** PROFILE TAB **********/

    private fun showProfileOptions(view: View) {

        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.profile_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->

            when (item.itemId) {

                R.id.menu_profile -> {

                    toolbarTitle.text = "Profile"
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
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
