package com.lange.facilitytracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lange.facilitytracker.data.model.TaskResources
import com.lange.facilitytracker.data.model.TaskTypeEnum

@Dao
interface FacilityTrackerDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskResources)

    @Query("DELETE FROM tasks_table WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM tasks_table")
    fun getAllTasks(): List<TaskResources>

    @Query("SELECT * FROM tasks_table WHERE taskType = :taskType")
    fun getTaskByType(taskType: TaskTypeEnum): List<TaskResources>

}