package com.lange.facilitytracker.data.model

data class Job(
    val _id: String?,
    val id_user: List<String?>?,
    val id_creator: String?,
    val id_address: String?,
    val job_type: Int?,
    val jobs_done: List<Task>?,
    val startTime: Long?,
    val endTime: Long?,
    val startLocation: GeoData?,
    val endLocation: GeoData?,
    var address: Address? = null

)
