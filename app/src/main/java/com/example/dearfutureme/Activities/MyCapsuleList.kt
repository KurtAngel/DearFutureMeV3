package com.example.dearfutureme.Activities

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.API.TokenManager
import com.example.dearfutureme.Adapter.CapsuleAdapter
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.Model.LogoutResponse
import com.example.dearfutureme.R
import com.example.dearfutureme.ViewModel.MainViewModel
import com.example.dearfutureme.databinding.ActivityMyCapsuleListBinding
import com.example.dearfutureme.fragments.HomeFragment
import com.example.dearfutureme.fragments.NotificationFragment
import com.example.dearfutureme.fragments.ShareCapsuleFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class MyCapsuleList : AppCompatActivity() {

    private lateinit var binding: ActivityMyCapsuleListBinding
    private val viewModel = MainViewModel()
    private val capsules = mutableListOf<Capsules>()

    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMyCapsuleListBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        replaceFragment(HomeFragment())
        bottomNavigation()
//        displayUsername()
//        initCapsuleList()
        addCapsuleBtn()
//        setGradient()
//        logoutBtn()
    }

    private fun addCapsuleBtn() {
        binding.addCapsuleBtn.setOnClickListener {
            startActivity(Intent(this@MyCapsuleList, CreateCapsule::class.java))
        }
    }

    private fun bottomNavigation() {

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.sharedCapsules -> {
                    replaceFragment(ShareCapsuleFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment : Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

//    private fun displayUsername() {
//        val username = intent.getStringExtra("USERNAME")
//        if (username != null) {
//            binding.usernameView.text = username // Display the username
//        }
//    }
//
//    private fun initCapsuleList() {
//
//        binding.progressBarCapsuleList.visibility = View.VISIBLE
//        viewModel.capsuleList.observe(this, Observer{
//            binding.recyclerViewCapsule.layoutManager =
//                LinearLayoutManager(
//                    this@MyCapsuleList,
//                    LinearLayoutManager.VERTICAL,
//                    false
//                )
//            binding.recyclerViewCapsule.adapter = CapsuleAdapter(capsules.toMutableList())
//            binding.progressBarCapsuleList.visibility = View.GONE
//        })
//        viewModel.loadCapsules()
//    }
//
//    private fun logoutBtn() {
//        binding.logoutBtn.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("Logout")
//            builder.setMessage("Are you sure you want to logout?")
//
//            builder.setPositiveButton("Yes") { dialog, which ->
//                logoutUser()
//            }
//
//            builder.setNegativeButton("No") { dialog, which ->
//                dialog.dismiss()
//            }
//
//            val dialog = builder.create()
//            dialog.show()
//        }
//    }
//
//    private fun logoutUser() {
//        tokenManager = TokenManager(this)
//
//        RetrofitInstance.instance.logout().enqueue(object : Callback<LogoutResponse> {
//            override fun onResponse(
//                call: Call<LogoutResponse>,
//                response: Response<LogoutResponse>
//            ) {
//                if (response.isSuccessful) {
//                    // Clear the token from SharedPreferences
//                    val logoutResponse = response.body()?.message
//                    tokenManager.clearToken()
//                    // Handle successful logout (e.g., show a success message)
//                    Toast.makeText(this@MyCapsuleList, "$logoutResponse", Toast.LENGTH_SHORT).show()
//
//                    // Redirect to the Login Activity
//                    val intent = Intent(this@MyCapsuleList, MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
//                    finish()
//                } else {
//                    // Handle the error response
//                    Toast.makeText(this@MyCapsuleList, "Logout failed: ${response.message()}", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
//                // Handle the failure case (e.g., network issues)
//                Toast.makeText(this@MyCapsuleList, "Logout failed: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun setGradient() {
//        val paint = binding.tvMyCapsule.paint
//        val width = paint.measureText(binding.tvMyCapsule.text.toString())
//        binding.tvMyCapsule.paint.shader = LinearGradient(
//            0f,0f,width,binding.tvMyCapsule.textSize, intArrayOf(
//                Color.parseColor("#6B26D4"),
//                Color.parseColor("#C868FF")
//            ), null, Shader.TileMode.CLAMP
//        )
//    }
//
//    private fun addCapsuleBtn() {
//        binding.addCapsuleBtn.setOnClickListener {
//            startActivity(Intent(this@MyCapsuleList, CreateCapsule::class.java))
//        }
//    }
}