package com.lange.facilitytracker.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import android.util.Log

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        Log.i("Interceptor",response.code.toString())
        Log.i("Interceptor-response", response.body.toString())

        // Log cookies received from the server
        if (response.headers("Set-Cookie").isNotEmpty()) {
            Log.d("CookieInterceptor", "Received cookies:")
            response.headers("Set-Cookie").forEach {
                Log.d("CookieInterceptor", it)
            }
        }

        return response
    }
}
