package com.lange.facilitytracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lange.facilitytracker.data.model.Address
import com.lange.facilitytracker.data.model.TaskResources

@Database(entities = [TaskResources::class, Address::class], version = 2)
abstract class FacilityTrackerDatabase : RoomDatabase() {

    abstract val dao: FacilityTrackerDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: FacilityTrackerDatabase? = null

        fun get(context: Context): FacilityTrackerDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FacilityTrackerDatabase::class.java,
                        "facilityTracker_db"
                    )
                        .addMigrations(MIGRATION_1_2)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE address_table (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, address TEXT NOT NULL)")
            }
        }
    }
}
