package com.example.dearfutureme.Model

data class Capsules(
    val id : Int,
    val title : String,
    val message : String,
    val content : String?,
    val receiver_email : String?,
    val schedule_open_at : String?,
    val draft : String?
)
