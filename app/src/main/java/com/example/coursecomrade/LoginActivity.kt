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
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity





/*****************************************************/
/*************** LoginActivity.kt JAVA ***************/
/*****************************************************/


/********** LOGIN ACTIVITY CLASS **********/

class LoginActivity : AppCompatActivity() {

    /***** set variables *****/

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button


    /********** CREATE PAGE **********/

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        buttonLogin = findViewById<Button>(R.id.buttonLogin)

        buttonLogin.setOnClickListener(View.OnClickListener {

            val username = editTextUsername.getText().toString()
            val password = editTextPassword.getText().toString()

            if (validateCredentials(username, password)) {

                val intent = Intent(

                    this@LoginActivity,
                    MainActivity::class.java
                )

                startActivity(intent)
                finish()

            } else {

                Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        })
    }


    /********** VALIDATE CREDENTIALS **********/

    private fun validateCredentials(username: String, password: String): Boolean {

        // TODO implement authentication mechanism

        return username == "user" && password == "password"
    }
}