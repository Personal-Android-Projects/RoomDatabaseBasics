package com.example.roomdatabase

import android.app.Application

//This is meant to create the database
//The Application class must be defined in the android manifest under android:name in the <application></application>
//The Singleton pattern used implemented the getInstance() method which requires,as a parameter, a context
//The this application app is created to meet that requirement
class EmployeeApp : Application() {
    val db by lazy{
        EmployeeDataBase.getInstance(this)
    }
}