package com.example.dearfutureme.APIResponse

import com.example.dearfutureme.Model.User

data class SignUpResponse(
    val status: String,
    val data: User?,
    val token: String
)
