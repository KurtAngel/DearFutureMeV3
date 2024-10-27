package com.example.dearfutureme.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dearfutureme.Model.Capsules

class CapsuleViewModel: ViewModel() {
    val _draftCapsule = MutableLiveData<Int>()
    val _sentCapsule = MutableLiveData<Int>()
    val _email = MutableLiveData<String>()
    val _username = MutableLiveData<String>()

    val username: LiveData<String> = _username

    fun setDraftCapsule(value: Int) {
        _draftCapsule.value = value
    }

    fun setSentCapsule(value: Int) {
        _sentCapsule.value = value
    }

    fun setEmail(value: String) {
        _email.value = value
    }

    fun setUsername(value: String) {
        _username.value = value
        Log.d("username", "Username: ${_username.value}")
    }

}