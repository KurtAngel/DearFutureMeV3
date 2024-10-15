package com.example.dearfutureme.Model

data class ReceivedCapsule(
    val Id : Int,
    val title : String,
    val message : String,
    val content : String,
    val receiver_email : String,
    val schedule_open_at : String
)
