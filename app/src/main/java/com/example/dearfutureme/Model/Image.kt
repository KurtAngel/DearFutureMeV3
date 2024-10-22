package com.example.dearfutureme.Model

import com.google.gson.annotations.SerializedName

data class Image(
    val id: Int,
    val image: String,
    @SerializedName("capsule_id")
    val capsuleId: Int,
    @SerializedName("capsule_type")
    val capsuleType: String
)
