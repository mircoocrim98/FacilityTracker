package com.lange.facilitytracker.data.model

import java.net.PasswordAuthentication

data class User(
    val authentication: Authentication?,
    val username: String?,
    val password: String?,
    val email: String?
)
