package com.example.dearfutureme.API

import com.example.dearfutureme.APIResponse.CapsuleResponse
import com.example.dearfutureme.APIResponse.DeletedCapsuleResponse
import com.example.dearfutureme.APIResponse.LoginResponse
import com.example.dearfutureme.APIResponse.LogoutResponse
import com.example.dearfutureme.APIResponse.ProfilePicResponse
import com.example.dearfutureme.APIResponse.ReceivedCapsuleResponse
import com.example.dearfutureme.APIResponse.SignUpResponse
import com.example.dearfutureme.APIResponse.UploadResponse
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.Model.ReceivedCapsule
import com.example.dearfutureme.Model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

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
    @POST("profile/upload")
    fun uploadProfileImage(@Part image: MultipartBody.Part): Call<UploadResponse>

    @Multipart
    @POST("capsules")
    fun createCapsule(
        @Part images: List<MultipartBody.Part>?,  // List of image parts for multiple images
        @Part("title") title: RequestBody,        // Other form fields
        @Part("message") message: RequestBody,
        @Part("receiver_email") receiverEmail: RequestBody,
        @Part("scheduled_open_at") scheduledOpenAt: RequestBody
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

    @GET("receivedCapsules")
    fun getReceivedCapsuleList(): Call<ReceivedCapsuleResponse>

    @GET("receivedCapsules/{id}")
    fun getReceivedCapsuleById(@Path("id") id: Int): Call<ReceivedCapsule>

    @Multipart
    @POST("receivedCapsules")
    fun sendCapsule(
        @Part images: List<MultipartBody.Part>?,  // List of image parts for multiple images
        @Part("title") title: RequestBody,        // Other form fields
        @Part("message") message: RequestBody,
        @Part("receiver_email") receiverEmail: RequestBody,
        @Part("scheduled_open_at") scheduledOpenAt: RequestBody
    ): Call<Capsules>

    @Multipart
    @POST("capsules/{capsule}/images")
    fun addImage(
        @Path("capsule") capsuleId: Int,
        @Part image: MultipartBody.Part
    ): Call<Capsules>

    @DELETE("images/{imageId}")
    fun deleteImage(@Path("imageId") imageId: Int): Call<DeletedCapsuleResponse>

    @DELETE("receivedCapsules/{id}")
    fun deleteReceivedCapsule(@Path("id") id: Int): Call<DeletedCapsuleResponse>

    @Headers("Content-Type: application/json")
    @GET("profile/show")
    fun getProfilePic() : Call<ProfilePicResponse>
}
