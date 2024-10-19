package com.example.dearfutureme.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize Retrofit and TokenManager
        RetrofitInstance.init(this)
        tokenManager = TokenManager(this)

        setupListeners()
    }

    private fun setupListeners() {
        // Set up login and account creation button click listeners
        binding.loginBtn.setOnClickListener {
            handleLogin()
        }
        binding.createAccount.setOnClickListener {
            startActivity(Intent(this@MainActivity, SignupUi::class.java))
        }
    }

    private fun handleLogin() {
        val email = binding.etEmailAddress.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (validateInputs(email, password)) {
            performLogin(User(null, email, password))
        } else {
            displayError("Please enter a valid Email and Password")
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun performLogin(user: User) {
        RetrofitInstance.instance.loginUser(user).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    handleSuccessfulLogin(response.body()!!)
                } else {
                    displayError("Login Failed, Try Again!")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Login Error", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Login error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleSuccessfulLogin(loginResponse: LoginResponse) {
        val token = loginResponse.token
        val username = loginResponse.user?.name

        token?.let {
            RetrofitInstance.setToken(it)
            tokenManager.saveToken(applicationContext, it)

            val intent = Intent(this@MainActivity, MyCapsuleList::class.java).apply {
                putExtra("USERNAME", "Hello $username!")
            }
            startActivity(intent)
        } ?: run {
            displayError("Token missing, unable to login.")
        }
    }

    private fun displayError(message: String) {
        binding.tvInvalid.text = message
        hideMessageAfterDelay()
    }

    private fun hideMessageAfterDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvInvalid.text = ""
        }, 3000)
    }
}

