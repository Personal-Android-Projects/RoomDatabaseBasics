package com.example.roomdatabase

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDAO {
    //Co-routines are used since the operations take a lot of time to execute
    @Insert
    suspend fun insert(employeeEntity: EmployeeEntity)

    @Update
    suspend fun update(employeeEntity : EmployeeEntity)

    @Delete
    suspend fun delete(employeeEntity : EmployeeEntity)

    //Query functions are not supposed to be used with the suspend keyword {This may have something to do with the fact that a Cursor type is returned}
    @Query("SELECT * FROM `employee-table`")
    fun fetchAllEmployees(): Flow<List<EmployeeEntity>>
    //Flow is used to hold values that can change at runtime

    @Query("SELECT * FROM `employee-table` WHERE id=:id")
    fun fetchEmployeeById(id : Int) : Flow<EmployeeEntity>

}