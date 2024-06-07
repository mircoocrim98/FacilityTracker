package com.lange.facilitytracker

import android.app.Application
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lange.facilitytracker.data.Repository
import com.lange.facilitytracker.data.model.Address
import com.lange.facilitytracker.data.model.AuthenticationPayload
import com.lange.facilitytracker.data.model.GeoData
import com.lange.facilitytracker.data.model.Job
import com.lange.facilitytracker.data.model.LoginRequest
import com.lange.facilitytracker.data.model.RegisterRequest
import com.lange.facilitytracker.data.model.TaskResources
import com.lange.facilitytracker.data.remote.FacilityTrackerApi
import kotlinx.coroutines.launch
import com.lange.facilitytracker.data.local.FacilityTrackerDatabase

enum class TypeOfWorkEnum {
    cleaning, maintenance, damagereport
}

public class MainViewModel(application: Application): AndroidViewModel(application) {

    private val database = FacilityTrackerDatabase.get(application)
    private val repository = Repository(FacilityTrackerApi, database)

    val loginResponse = repository.loginResponse
    val registerResponse = repository.registerResponse
    val nearbyAddresses = repository.nearbyAddresses
    val jobById = repository.jobById
    val createJobResponse = repository.createJobResponse
    val errorMessage = repository.errorMessage
    val taskResources = repository.taskResources

    var currentTypeOfWork: TypeOfWorkEnum? = null
    var currentUserId: String? = null
    private val _currentAddress = MutableLiveData<Address>()

    val currentAddress: LiveData<Address>
        get() = _currentAddress
    var geoData: GeoData? = null



    fun setCurrentAddress(address: Address){
        _currentAddress.postValue(address)
    }

    fun register(request: RegisterRequest){
        viewModelScope.launch {
            repository.register(request)
        }
    }

    fun login(request: LoginRequest){
        viewModelScope.launch {
           repository.login(request)
        }
    }

    fun getUsers(){
        viewModelScope.launch {
            repository.getUsers()
        }
    }

    fun sendToken(sessionToken: AuthenticationPayload){
        viewModelScope.launch {
            repository.getCurrentUser(sessionToken)
        }
    }

    fun getAdressByGeoData(geoData: GeoData){
        viewModelScope.launch {
            repository.getNearbyAddressesByGeoData(geoData)
        }
    }

    fun getJobByUserId(userId: String){
        viewModelScope.launch {
            repository.jobById
        }
    }

    fun createJob(job: Job){
        viewModelScope.launch {
            repository.createJob(job)
        }
    }

    fun insertTaskInDB(taskResources: TaskResources){
        viewModelScope.launch {
            repository.insertTaskInDB(taskResources)
        }
    }

    fun insertAllTasksInDB(taskResources: List<TaskResources>){
        viewModelScope.launch {
            repository.insertAllTasksInDB(taskResources)
        }
    }

    fun deleteByIdFromDB(id: Long){
        viewModelScope.launch {
            repository.deleteByIdFromDB(id)
        }
    }

    fun getAllTasksFromDB(){
        viewModelScope.launch {
            repository.getAllTasksFromDB()
        }
    }

    fun getAllTasksByTypeOfWork(taskType: Int){
        viewModelScope.launch {
            repository.getAllTasksByTypeOfWork(taskType)
        }
    }
}