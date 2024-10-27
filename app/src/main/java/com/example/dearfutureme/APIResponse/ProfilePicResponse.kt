package com.example.dearfutureme.APIResponse

import com.google.gson.annotations.SerializedName
import retrofit2.http.Url

data class ProfilePicResponse (
    @SerializedName("profile_pic_url")
    val profilePicUrl: String? = null
)
