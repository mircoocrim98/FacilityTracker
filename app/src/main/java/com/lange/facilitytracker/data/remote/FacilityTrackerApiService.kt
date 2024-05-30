package com.lange.facilitytracker.data.remote

import com.lange.facilitytracker.data.model.Address
import com.lange.facilitytracker.data.model.AuthenticationPayload
import com.lange.facilitytracker.data.model.GeoData
import com.lange.facilitytracker.data.model.Job
import com.lange.facilitytracker.data.model.LoginRequest
import com.lange.facilitytracker.data.model.RegisterRequest
import com.lange.facilitytracker.data.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.http.Path

const val BASE_URL = "https://keepy-api-production.up.railway.app"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okHttpClient = OkHttpClient.Builder()
    .cookieJar(SimpleCookieJar())
    .addInterceptor(LoggingInterceptor())
    .addInterceptor(RequestLoggingInterceptor())
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface FacilityTrackerApiService{

    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<User>

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<User>

    @POST("/auth/token")
    suspend fun getCurrentUser(@Body sessionToken: AuthenticationPayload): Response<User>

    @GET("/users")
    suspend fun getUsers() : List<User>

    @POST("/adresses/find")
    suspend fun getNearbyAddressByGeoData(@Body geoData: GeoData) : Response<List<Address>>

    @GET("/jobs/{userId}")
    suspend fun getJobByUserId(@Path("userId") userId: String) : Response<Job>

    @POST("/jobs")
    suspend fun createJob(@Body job: Job) : Response<Job>

}

object FacilityTrackerApi {
    val retrofitService: FacilityTrackerApiService by lazy { retrofit.create(FacilityTrackerApiService::class.java) }
}