package com.example.dearfutureme.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.Model.Capsules
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _capsuleList =  MutableLiveData<MutableList<Capsules>>()

    val capsuleList: LiveData<MutableList<Capsules>> = _capsuleList

    private val apiService = RetrofitInstance.instance

    fun loadCapsules() {
        apiService.getCapsuleList().enqueue(object : Callback<List<Capsules>> {
            override fun onResponse(
                call: Call<List<Capsules>>,
                response: Response<List<Capsules>>
            ) {
                if (response.isSuccessful){
                    _capsuleList.value = response.body()?.toMutableList() ?: mutableListOf()
                } else {
                    Log.d("API", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Capsules>>, t: Throwable) {
                Log.e("Log Error", "Error: ${t.message}")
            }
        })
    }
}