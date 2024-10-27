package com.example.dearfutureme.API

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.1.3:8000/api/"

    // TokenManager instance
    lateinit var tokenManager: TokenManager

    // Initialize TokenManager
    fun init(context: Context) {
        tokenManager = TokenManager(context)
    }

    // Build OkHttpClient with interceptor to add the token
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder: Request.Builder = chain.request().newBuilder()

                // Get the token from TokenManager
                val token = tokenManager.getToken()
                token?.let {
                    requestBuilder.addHeader("Authorization", "Bearer $it")
                }

                chain.proceed(requestBuilder.build())
            }.build()
    }

    // Retrofit instance
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}
