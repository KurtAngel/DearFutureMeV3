// RetroInstance.kt
package com.example.dearfutureme.API

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8000/api/" // Ensure the endpoint is correct

    lateinit var tokenManager: TokenManager

    private var authToken: String? = null

    fun setToken(token: String) {
        authToken = token
    }

    fun init(context: Context) {
        tokenManager = TokenManager(context)
    }
    // Build http client with interceptor
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder: Request.Builder = chain.request().newBuilder()

                val token = tokenManager.getToken()
                token?.let {
                    requestBuilder.addHeader("Authorization", "Bearer $it")
                }

                chain.proceed(requestBuilder.build())
            }.build()
    }

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            retrofit.create(ApiService::class.java)
    }
}