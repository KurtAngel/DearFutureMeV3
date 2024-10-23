package com.example.dearfutureme.API

import com.example.dearfutureme.APIResponse.CapsuleResponse
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.APIResponse.DeletedCapsuleResponse
import com.example.dearfutureme.APIResponse.LoginResponse
import com.example.dearfutureme.APIResponse.LogoutResponse
import com.example.dearfutureme.APIResponse.SignUpResponse
import com.example.dearfutureme.Model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("capsules")
    fun createCapsule(
        @Part images: List<MultipartBody.Part>?,  // List of image parts for multiple images
        @Part("title") title: RequestBody,        // Other form fields
        @Part("message") message: RequestBody,
        @Part("receiver_email") receiverEmail: RequestBody,
        @Part("scheduled_open_at") scheduledOpenAt: RequestBody,
        @Part("draft") draft: RequestBody?
    ): Call<Capsules>

    @GET("capsules")
    fun getCapsuleList(): Call<CapsuleResponse>

    @GET("capsules/{id}")
    fun getCapsuleById(@Path("id") id: Int): Call<Capsules>

    @DELETE("capsules/{id}")
    fun deleteCapsule(@Path("id") id: Int): Call<DeletedCapsuleResponse>

    @Headers("Content-Type: application/json")
    @PUT("capsules/{id}")
    fun updateCapsule(@Path("id") id: Int, @Body capsule: Capsules): Call<Capsules>
}
