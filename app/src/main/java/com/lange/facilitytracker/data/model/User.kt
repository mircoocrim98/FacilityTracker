package com.lange.facilitytracker.data.model

data class User(
    val _id: String,
    val authentication: AuthenticationPayload?,
    val username: String,
    val email: String
)
