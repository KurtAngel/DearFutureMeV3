package com.example.dearfutureme.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dearfutureme.Model.Capsules

class CapsuleViewModel: ViewModel() {
    val selectedCapsule = MutableLiveData<Capsules>()
}