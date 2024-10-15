package com.example.dearfutureme.Model

data class LoginResponse(
    val token: String,
    val message: String,
    val user: User?
)
