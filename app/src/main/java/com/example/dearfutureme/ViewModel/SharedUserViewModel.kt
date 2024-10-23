package com.example.dearfutureme.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dearfutureme.Model.User

class SharedUserViewModel : ViewModel() {

    // Encapsulate the MutableLiveData to allow external classes to observe, but not modify
    private val _user = MutableLiveData<User>()

    // Expose the LiveData to be observed
    val user: LiveData<User> get() = _user

    // Method to update the user
    fun setUser(newUser: User) {
        _user.value = newUser
    }
}
