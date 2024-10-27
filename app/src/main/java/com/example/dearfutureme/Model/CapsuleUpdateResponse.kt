package com.example.dearfutureme.Model

import com.google.gson.annotations.SerializedName

data class CapsuleUpdateResponse(
    val message: String,
    val title: String,
    @SerializedName("receiver_email")
    val receiverEmail: String,
    @SerializedName("scheduled_open_at")
    val scheduledOpenAt: String,
)
