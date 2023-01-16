package com.example.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Can not create an object from this class
@Database(entities = [EmployeeEntity::class],version = 1)
//If you change a property of a database ie{The number of columns in a table the version is supposed to be changed}
abstract class EmployeeDataBase : RoomDatabase(){
    //Used to connect database to employeeDao
    abstract fun employeeDao():EmployeeDAO
    //Companion object that is used to instantiate the database
    companion object{
        @Volatile
        private var INSTANCE : EmployeeDataBase? = null
        //Volatile means that all changes to data are from main memory so that the changes are seen by all threads

        //Singleton pattern is used here where only one instance of a class exists
        fun getInstance(context:Context) :EmployeeDataBase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    //This method of creating the database is that it destroys and rebuilds the database when changes to the database structure occur instead of migrating
                    instance = Room.databaseBuilder(context.applicationContext,EmployeeDataBase::class.java,"employee_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}