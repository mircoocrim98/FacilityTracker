package com.lange.facilitytracker.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lange.facilitytracker.data.local.FacilityTrackerDatabase
import com.lange.facilitytracker.data.model.Address
import com.lange.facilitytracker.data.model.AuthenticationPayload
import com.lange.facilitytracker.data.model.GeoData
import com.lange.facilitytracker.data.model.Job
import com.lange.facilitytracker.data.model.LoginRequest
import com.lange.facilitytracker.data.model.RegisterRequest
import com.lange.facilitytracker.data.model.TaskResources
import com.lange.facilitytracker.data.model.TaskTypeEnum
import com.lange.facilitytracker.data.model.User
import com.lange.facilitytracker.data.remote.FacilityTrackerApi
import retrofit2.Response

const val TAG = "Repository"

class Repository(
    private val api: FacilityTrackerApi,
    private val database: FacilityTrackerDatabase
) {

    private val _loginResponse = MutableLiveData<Response<User>>()
    val loginResponse: LiveData<Response<User>>
        get() = _loginResponse

    private val _registerResponse = MutableLiveData<Response<User>>()
    val registerResponse: LiveData<Response<User>>
        get() = _registerResponse

    private val _nearbyAddresses = MutableLiveData<Response<List<Address>>>()
    val nearbyAddresses: LiveData<Response<List<Address>>>
        get() = _nearbyAddresses


    private val _jobById = MutableLiveData<Response<Job>>()
    val jobById: LiveData<Response<Job>>
        get() = _jobById


    private val _createJobResponse = MutableLiveData<Response<Job>>()
    val createJobResponse: LiveData<Response<Job>>
        get() = _createJobResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _taskResources = MutableLiveData<TaskResources>()
    val taskResources: LiveData<TaskResources>
        get() = _taskResources

    suspend fun insertInDB(taskResources: TaskResources){
        try {
            database.dao.insertTask(taskResources)
        } catch (e: Exception){
            log("Cant insert taskresource: ", e)
        }
    }

    suspend fun deleteByIdFromDB(id: Long){
        try {
            database.dao.deleteById(id)
        } catch (e: Exception){
            log("Cant delete taskresource", e)
        }
    }

    suspend fun getAllTasksFromDB(){
        try {
            database.dao.getAllTasks()
        } catch (e: Exception){
            log("Cant get all tasks", e)
        }
    }

    suspend fun getAllTasksByTypeOfWork(taskTypeEnum: TaskTypeEnum){
        try {
            database.dao.getTaskByType(taskTypeEnum)
        } catch (e: Exception){
            log("Cant get tasks by type of work", e)
        }
    }

    suspend fun register(request: RegisterRequest){
        try {
            val result = api.retrofitService.register(request)
            _registerResponse.postValue(result)
        } catch (e: Exception){
            log("Cant register user", e)
        }
    }

    suspend fun login(request: LoginRequest){
        try {
            val result = api.retrofitService.login(request)
            _loginResponse.postValue(result)
        } catch (e: Exception){
            log("cant login", e)

        }
    }

    suspend fun getUsers(){
        try {
            api.retrofitService.getUsers()
        } catch (e: Exception){
            log("Cant get Users", e)
        }
    }

    suspend fun getCurrentUser(sessionToken: AuthenticationPayload){
        try {
            val result = api.retrofitService.getCurrentUser(sessionToken)
            _loginResponse.postValue(result)
        } catch (e: Exception){
            log("Cant send token", e)
        }
    }

    suspend fun getNearbyAddressesByGeoData(geoData: GeoData){
        try {
            val result = api.retrofitService.getNearbyAddressByGeoData(geoData)
            _nearbyAddresses.postValue(result)
        } catch (e: Exception){
            log("Cant get nearby addresses by geo data", e)
        }
    }

    suspend fun getJobByUserId(userID: String){
        try {
            val result = api.retrofitService.getJobByUserId(userID)
            _jobById.postValue(result)
        } catch (e: Exception) {
            log("Cant get user by id", e)        }
    }

    suspend fun createJob(job: Job){
        try {
            val result = api.retrofitService.createJob(job)
            _createJobResponse.postValue(result)
        } catch (e: Exception){
            log("Cant create job", e)
        }
    }

    fun log(message: String, e: Exception){
        Log.e(TAG, "$message: $e")
        _errorMessage.postValue(message)
    }
}