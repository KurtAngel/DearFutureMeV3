package com.example.dearfutureme.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dearfutureme.API.ApiService
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.API.TokenManager
import com.example.dearfutureme.Model.LoginResponse
import com.example.dearfutureme.Model.User
import com.example.dearfutureme.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        RetrofitInstance.init(this)
        tokenManager = TokenManager(this)

        loginAcc()
        createAcc()
    }

    private fun loginAcc() {
        binding.loginBtn.setOnClickListener {
            val email = binding.etEmailAddress.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val request = User(null , email, password)

                RetrofitInstance.instance.loginUser(request).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            val token = response.body()?.token
                            // Save token if not null
                            if (token != null) {
                                RetrofitInstance.setToken(token)
                                tokenManager.saveToken(applicationContext, token)
                            }
                            startActivity(Intent(this@MainActivity, MyCapsuleList::class.java))
                        } else {
                            Toast.makeText(this@MainActivity, "Login failed ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Login error: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("Login Error", "Error: ${t.message}")
                    }
                })
            } else {
                binding.tvInvalid.text = "Invalid credentials!"
                hideMessageAfterDelay()
            }
        }
    }

    private fun createAcc() {
        binding.createAccount.setOnClickListener {
            startActivity(Intent(this@MainActivity, SignupUi::class.java))
        }
    }

    private fun hideMessageAfterDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvInvalid.text = ""
        }, 3000)
    }

}
