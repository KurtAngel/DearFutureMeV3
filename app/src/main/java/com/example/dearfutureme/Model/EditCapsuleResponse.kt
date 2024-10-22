package com.example.dearfutureme.Model

data class EditCapsuleResponse(
    val updateMessage: String,
    val id : Int,
    val title : String,
    val message : String,
    val content : String?,
    val receiverEmail : String?,
    val scheduleOpenAt : String?
)
