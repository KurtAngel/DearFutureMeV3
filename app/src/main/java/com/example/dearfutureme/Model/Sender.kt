package com.example.dearfutureme.Model

import com.google.gson.annotations.SerializedName

data class Sender(
    val name: String,
    val email: String,
    @SerializedName("profile_pic_url")
    val profilePicUrl: String?
)
