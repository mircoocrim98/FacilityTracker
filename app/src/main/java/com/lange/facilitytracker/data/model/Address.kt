package com.lange.facilitytracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address_table")
data class Address(
    @PrimaryKey (autoGenerate = false)
    val _id: String,
    val Bezeichnung: String?,
    val Adresse: String?,
)
