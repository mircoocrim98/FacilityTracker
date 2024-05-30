package com.lange.facilitytracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_table")
data class TaskResources (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    var taskType: Enum<TaskTypeEnum>
)

enum class TaskTypeEnum{
    cleaning, maintenance, damagereport
}