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


    private val _jobsById = MutableLiveData<List<Job>>()
    val jobsById: LiveData<List<Job>>
        get() = _jobsById


    private val _createJobResponse = MutableLiveData<Response<Job>>()
    val createJobResponse: LiveData<Response<Job>>
        get() = _createJobResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _taskResources = MutableLiveData<List<TaskResources>>()
    val taskResources: LiveData<List<TaskResources>>
        get() = _taskResources

    private val _updateJobResponse = MutableLiveData<Response<Job>>()
    val updateJobResponse: LiveData<Response<Job>>
        get() = _updateJobResponse

    private val _addresses = MutableLiveData<List<Address>>()
    val addresses: LiveData<List<Address>>
        get() = _addresses

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    suspend fun insertAllAddressesInDB(){
        try {
            val result = api.retrofitService.getAllAddresses()
            database.dao.insertAllAddresses(result)
        } catch (e: Exception){
            log("Cant insert all addresses", e)
        }
    }

    suspend fun getAllAddressesFromDB(){
        try {
            val result = database.dao.getAllAddresses()
            _addresses.postValue(result)
        } catch (e: Exception){
            log("Cant get all addresses", e)
        }
    }

    suspend fun getAddressByIdFromDB(id: String){
        try {
            val result = database.dao.getAddress(id)
            _address.postValue(result)
        } catch (e: Exception){
            log("Cant get address", e)
        }
    }

    suspend fun insertTaskInDB(taskResources: TaskResources){
        try {
            database.dao.insertTask(taskResources)
        } catch (e: Exception){
            log("Cant insert taskresource: ", e)
        }
    }

    suspend fun insertAllTasksInDB(taskResources: List<TaskResources>){
        try {
            val result = database.dao.insertAllTasks(taskResources)
        } catch (e: Exception){
            log("Cant insert al taskresources:", e)
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
            val result = database.dao.getAllTasks()
            _taskResources.postValue(result)
        } catch (e: Exception){
            log("Cant get all tasks", e)
        }
    }

    suspend fun getAllTasksByTypeOfWork(taskType: Int){
        try {
            val result = database.dao.getTaskByType(taskType)
            _taskResources.postValue(result)
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

    suspend fun getJobsByUserId(userID: String){
        try {
            val result = api.retrofitService.getJobsByUserId(userID).body() ?: emptyList()
            result.onEach{
                it.address = it.id_address?.let { it1 -> database.dao.getAddress(it1) }
            }
            _jobsById.postValue(result)
        } catch (e: Exception) {
            log("Cant get jobs by id", e)
        }
    }

    suspend fun createJob(job: Job){
        try {
            val result = api.retrofitService.createJob(job)
            _createJobResponse.postValue(result)
        } catch (e: Exception){
            log("Cant create job", e)
        }
    }

    suspend fun updateJob(jobID: String, job: Job){
        try {
            val result = api.retrofitService.updateJob(jobID, job)
            _updateJobResponse.postValue(result)
        } catch (e: Exception){
            log("Cant update job", e)
        }
    }

    fun log(message: String, e: Exception){
        Log.e(TAG, "$message: $e")
        _errorMessage.postValue(message)
    }
}