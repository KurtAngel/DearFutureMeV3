package com.example.dearfutureme.Model

import okhttp3.MultipartBody

data class ImageData(
    val imageUrl: String,
    val imageFile: MultipartBody.Part? = null
)
