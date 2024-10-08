package com.lange.facilitytracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lange.facilitytracker.data.model.Address
import com.lange.facilitytracker.data.model.TaskResources

@Dao
interface FacilityTrackerDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskResources)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTasks(task: List<TaskResources>)

    @Query("DELETE FROM tasks_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM tasks_table")
    suspend fun getAllTasks(): List<TaskResources>

    @Query("SELECT * FROM tasks_table WHERE taskType = :taskType")
    suspend fun getTaskByType(taskType: Int): List<TaskResources>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAddresses(address: List<Address>)

    @Query("SELECT * FROM address_table WHERE _id = :id")
    suspend fun getAddress(id: String): Address

    @Query("SELECT * FROM address_table")
    suspend fun getAllAddresses(): List<Address>


}