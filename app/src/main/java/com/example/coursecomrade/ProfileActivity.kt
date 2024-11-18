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
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity





/*******************************************************/
/*************** ProfileActivity.kt JAVA ***************/
/*******************************************************/


/********** PROFILE ACTIVITY CLASS **********/

class ProfileActivity : AppCompatActivity() {


    /********** CREATE PAGE **********/

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
}
