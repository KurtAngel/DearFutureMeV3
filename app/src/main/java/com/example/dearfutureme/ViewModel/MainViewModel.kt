package com.example.dearfutureme.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.Model.CapsuleResponse
import com.example.dearfutureme.Model.Capsules
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _capsuleList = MutableLiveData<List<Capsules>>(emptyList())
    val capsuleList: LiveData<List<Capsules>> = _capsuleList

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadCapsules() {
        RetrofitInstance.instance.getCapsuleList().enqueue(object : Callback<CapsuleResponse> {

            override fun onResponse(call: Call<CapsuleResponse>, response: Response<CapsuleResponse>)
            {
                if (response.isSuccessful)
                {
                    _capsuleList.value = response.body()?.data
                }
                else
                {
                    _error.value = "Error: ${response.message()}"
                    Log.d("API", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CapsuleResponse>, t: Throwable) {
                _error.value = "Network Error: ${t.message}"
                Log.e("Log Error", "Network Error: ${t.message}")
            }
        })
    }

    // Optional: Function to reset error state after UI handles it
    fun clearError() {
        _error.value = null
    }
}
