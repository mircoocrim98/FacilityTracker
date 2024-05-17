package com.lange.facilitytracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lange.facilitytracker.data.Repository
import com.lange.facilitytracker.data.model.User
import com.lange.facilitytracker.data.remote.FacilityTrackerApi
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = Repository(FacilityTrackerApi)

    fun register(user: User){
        viewModelScope.launch {
            repository.register(user)
        }
    }

    fun login(user: User){
        viewModelScope.launch {
            repository.login(user)
        }
    }

    fun getUsers(){
        viewModelScope.launch {
            repository.getUsers()
        }
    }
}