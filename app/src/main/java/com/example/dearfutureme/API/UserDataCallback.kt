package com.example.dearfutureme.API

interface UserDataCallback {
    fun onUserDataFetched(userList: List<String>)
    fun onError(errorMessage: String)
}