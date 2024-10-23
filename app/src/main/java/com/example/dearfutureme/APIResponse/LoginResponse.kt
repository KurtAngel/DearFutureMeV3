package com.example.dearfutureme.APIResponse

import com.example.dearfutureme.Model.User

data class LoginResponse(
    val message: String,
    val user: User?,
    val token: String
)
