package com.example.dearfutureme.API

import android.app.Application
import com.example.dearfutureme.Model.CapsuleResponse
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.Model.EditCapsuleResponse
import com.example.dearfutureme.Model.LoginResponse
import com.example.dearfutureme.Model.LogoutResponse
import com.example.dearfutureme.Model.SignUpResponse
import com.example.dearfutureme.Model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("register")
    fun registerUser(@Body request: User): Call<SignUpResponse>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun loginUser(@Body request: User): Call<LoginResponse>

    @POST("logout") // Adjust the endpoint according to your API
    fun logout(): Call<LogoutResponse>

    @POST("capsules")
    fun createCapsule(@Body capsule: Capsules): Call<Capsules>

    @GET("capsules")
    fun getCapsuleList(): Call<CapsuleResponse>

    @GET("capsules/{id}")
    fun getCapsuleById(@Path("id") id: Int): Call<Capsules>

    @DELETE("capsules/{id}")
    fun deleteCapsule(@Path("id") id: Int): Call<Void>

    @PUT("capsules/{id}")
    fun updateCapsule(@Path("id") id: Int, @Body capsule: Capsules): Call<Capsules>
}
