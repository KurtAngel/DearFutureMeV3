package com.example.dearfutureme.Model

import com.google.gson.annotations.SerializedName

data class Image(
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("capsule_id")
    val capsuleId: Int,
    @SerializedName("capsule_type")
    val capsuleType: String
)
