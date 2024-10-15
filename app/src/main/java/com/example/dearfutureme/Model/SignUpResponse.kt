package com.example.dearfutureme.Model

data class SignUpResponse(
    val status: String,
    val data: User?,
    val token: String
)
