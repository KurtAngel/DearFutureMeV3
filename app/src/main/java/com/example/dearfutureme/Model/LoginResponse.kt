package com.example.dearfutureme.Model

data class LoginResponse(
    val message: String,
    val user: User?,
    val token: String
)
