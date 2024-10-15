package com.example.dearfutureme.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.Model.SignUpResponse
import com.example.dearfutureme.Model.User
import com.example.dearfutureme.databinding.ActivitySignupuiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupUi : AppCompatActivity() {

    private lateinit var binding: ActivitySignupuiBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupuiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginAcc()
        signup()
    }

    private fun signup() {
        binding.signupBtn.setOnClickListener {
            val username = binding.etuserName.text.toString()
            val email = binding.etEmailAddress.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                val request = User(username, email, password)

                RetrofitInstance.instance.registerUser(request).enqueue(object : Callback<SignUpResponse> {
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val userRegistration = response.body()?.status

                                binding.tvUserExist.text = "$userRegistration"
                                hideMessageAfterDelay()

                        } else {
                            binding.tvUserExist.text = "Registration Failed"
                            hideMessageAfterDelay()
                        }
                    }

                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        Log.e("Registration Error", "Error: ${t.message}")
                    }
                })
            }
        }
    }


    private fun hideMessageAfterDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvUserExist.text = ""
        }, 4000)
    }

    private fun loginAcc() {
        binding.loginAccount.setOnClickListener {
            startActivity(Intent(this@SignupUi, MainActivity::class.java))
        }
    }
}
