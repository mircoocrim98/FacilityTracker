package com.lange.facilitytracker.data.model

data class Job(
    val id_user: String?,
    val id_address: String?,
    val job_type: Int,
    val jobs_done: List<String?>,
    val startTime: Int,
    val endTime: Int,
    val startLocation: String,
    val endLocation: String
)
