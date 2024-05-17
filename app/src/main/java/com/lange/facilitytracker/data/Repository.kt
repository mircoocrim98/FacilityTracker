package com.lange.facilitytracker.data

import android.util.Log
import com.lange.facilitytracker.data.model.User
import com.lange.facilitytracker.data.remote.FacilityTrackerApi

const val TAG = "Repository"

class Repository(
    private val api: FacilityTrackerApi
) {

    suspend fun register(user: User){
        try {
            val response: Unit = api.retrofitService.register(user)
            Log.e(TAG, response.toString())
        } catch (e: Exception){
            Log.e(TAG, "Cant register User: $e")
        }
    }

    suspend fun login(user: User){
        try {
            api.retrofitService.login(user)
        } catch (e: Exception){
            Log.e(TAG, "Cant login: $e")
        }
    }

    suspend fun getUsers(){
        try {
            api.retrofitService.getUsers()
        } catch (e: Exception){
            Log.e(TAG,"Cant get Users $e")
        }
    }
}