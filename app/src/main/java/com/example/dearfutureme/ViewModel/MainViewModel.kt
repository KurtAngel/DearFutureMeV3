package com.example.dearfutureme.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.APIResponse.CapsuleResponse
import com.example.dearfutureme.APIResponse.ProfilePicResponse
import com.example.dearfutureme.APIResponse.ReceivedCapsuleResponse
import com.example.dearfutureme.DataRepository.ProfileRepository
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.Model.ReceivedCapsule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _capsuleList = MutableLiveData<List<Capsules>>(emptyList())
    private  val _receivedCapsuleList = MutableLiveData<List<ReceivedCapsule>>(emptyList())
    private val _imageGetter = MutableLiveData<String?>()

    // Base URL for your server
//    private val BASE_URL: String = "http://192.168.1.3:8000"

    val capsuleList: LiveData<List<Capsules>> = _capsuleList
    val receivedCapsuleList: LiveData<List<ReceivedCapsule>> = _receivedCapsuleList
    val imageGetter: LiveData<String?> = _imageGetter

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadCapsules() {
        RetrofitInstance.instance.getCapsuleList().enqueue(object : Callback<CapsuleResponse>
        {
            override fun onResponse(call: Call<CapsuleResponse>, response: Response<CapsuleResponse>)
            {
                if (response.isSuccessful) {
                    _capsuleList.value = response.body()?.data
                } else {
                    _error.value = "No Capsule Found, Create\na Capsule"
                    Log.d("API", "Error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CapsuleResponse>, t: Throwable) {
                _error.value = "Network Error: ${t.message}"
                Log.e("Log Error", "Network Error: ${t.message}")
            }
        })
    }

    fun loadReceivedCapsules() {
        RetrofitInstance.instance.getReceivedCapsuleList().enqueue(object : Callback<ReceivedCapsuleResponse>
        {
                override fun onResponse(call: Call<ReceivedCapsuleResponse>, response: Response<ReceivedCapsuleResponse>)
                {
                    if (response.isSuccessful) {
                        _receivedCapsuleList.value = response.body()?.data
                    } else {
                        _error.value = "No Capsules Found"
                        Log.d("API", "Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ReceivedCapsuleResponse>, t: Throwable) {
                    Log.e("Log Error", "Network Error: ${t.message}")
                }
            })

        // Optional: Function to reset error state after UI handles it
        fun clearError() {
            _error.value = null
        }
    }

    fun loadProfilePic() {
        RetrofitInstance.instance.getProfilePic().enqueue(object : Callback<ProfilePicResponse> {
            override fun onResponse(call: Call<ProfilePicResponse>, response: Response<ProfilePicResponse>)
            {
                if (response.isSuccessful){
                    ProfileRepository.imageGetter = response.body()?.profilePicUrl.toString()
                    _imageGetter.value = ProfileRepository.imageGetter
                    Log.d("Image", "Image: ${ProfileRepository.imageGetter}")
                } else {
                    Log.d("API", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProfilePicResponse>, t: Throwable) {
                Log.e("Log Error", "Network Error: ${t.message}")
            }
        })
    }

    fun loadDraftCount(){

    }

    fun loadSentCount(){

    }
}
