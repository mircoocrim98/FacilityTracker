package com.lange.facilitytracker.data.remote

import com.lange.facilitytracker.data.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import okhttp3.OkHttpClient

const val BASE_URL = "http://192.168.178.26:8080"

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
    suspend fun register(@Body user: User)

    @POST("/auth/login")
    suspend fun login(@Body user: User)

    @GET("/users")
    suspend fun getUsers() : List<User>

}

object FacilityTrackerApi {
    val retrofitService: FacilityTrackerApiService by lazy { retrofit.create(FacilityTrackerApiService::class.java) }
}