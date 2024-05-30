package com.lange.facilitytracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lange.facilitytracker.data.model.Task

@Database(entities = [Task::class], version = 1)
abstract class FacilityTrackerDatabase : RoomDatabase(){

    abstract val dao: FacilityTrackerDatabaseDao

    companion object{
        private lateinit var INSTANCE: FacilityTrackerDatabase

        fun get(context: Context) : com.lange.facilitytracker.data.local.FacilityTrackerDatabase {
            synchronized(FacilityTrackerDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FacilityTrackerDatabase::class.java,
                        "facilityTracker_db"
                    ).build()
                }
                return INSTANCE
            }
        }
    }
}