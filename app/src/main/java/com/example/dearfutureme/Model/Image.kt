package com.example.dearfutureme.Model

import com.google.gson.annotations.SerializedName

data class Image(
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String?
)
