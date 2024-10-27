package com.example.dearfutureme.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.API.TokenManager
import com.example.dearfutureme.APIResponse.LogoutResponse
import com.example.dearfutureme.DataRepository.ProfileRepository
import com.example.dearfutureme.DataRepository.UserRepository
import com.example.dearfutureme.R
import com.example.dearfutureme.ViewModel.CapsuleViewModel
import com.example.dearfutureme.ViewModel.MainViewModel
import com.example.dearfutureme.databinding.ActivityAccountSettingsBinding
import com.example.dearfutureme.fragments.HomeFragment
import com.example.dearfutureme.fragments.ShareCapsuleFragment
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.System.load

class MyCapsuleList : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSettingsBinding
    private var viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        setUpListeners()
        hideNavigationBar()
        bundleListener()
        navViewListener()

    }

    private fun bundleListener() {
        val username = UserRepository.username
        val email = UserRepository.email
        val navView: NavigationView = binding.navView
        val headerView = navView.getHeaderView(0)
        val usernameTextView = headerView.findViewById<TextView>(R.id.tvUserName)
        val emailTextView = headerView.findViewById<TextView>(R.id.tvEmail)
        val imageHolder = headerView.findViewById<ImageView>(R.id.profileViewer)
        usernameTextView.text = username.toString()
        emailTextView.text = email.toString()

        viewModel.loadProfilePic()
        viewModel.imageGetter.observe(this, Observer {
            Glide.with(this)
                .load(ProfileRepository.imageGetter)
                .placeholder(R.drawable.baseline_person_24)
                .error(R.drawable.baseline_person_24)
                .into(imageHolder)
        })
    }

    private fun navViewListener() {
        val drawerLayout: DrawerLayout = binding.drawerLayout

        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        // Set click listeners for navigation items
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profileSettings -> {
                    val intent = Intent(this, ProfileSettingsActivity::class.java)
                    this@MyCapsuleList.startActivity(intent)
                }
                R.id.nav_logout -> {
                    showLogoutDialog()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun showLogoutDialog() {
        // Inflate the custom dialog layout
        val customView = layoutInflater.inflate(R.layout.custom_logout_dialog, null)

        // Build the AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setView(customView)

        // Create and show the dialog
        val dialog = builder.create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Set up custom dialog's UI elements
        val yesButton = customView.findViewById<Button>(R.id.btnYes)
        val noButton = customView.findViewById<Button>(R.id.btnNo)

        yesButton.setOnClickListener {
            // Perform the logout operation
            logoutUser()
            dialog.dismiss()  // Dismiss the dialog
        }

        noButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun logoutUser() {
        RetrofitInstance.instance.logout().enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                if (response.isSuccessful) {
                    RetrofitInstance.tokenManager.clearToken()

                    ProfileRepository.imageGetter = null
                    ProfileRepository.imageHolder = null


                    Toast.makeText(this@MyCapsuleList, response.body()?.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MyCapsuleList, LoginActivity::class.java).apply {
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                } else {
                    Toast.makeText(this@MyCapsuleList, "Logout failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Toast.makeText(this@MyCapsuleList, "Logout failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNavigationBar()
        }
    }

    private fun hideNavigationBar() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    private fun setUpListeners() {
        binding.activityMyCapsuleList.addCapsuleBtn.setOnClickListener {
            val username = intent.getStringExtra("USERNAME")
            val intent = Intent(this@MyCapsuleList, CreateCapsule::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

        binding.activityMyCapsuleList.bottomNavigationView.setOnItemSelectedListener { item ->
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

    // Override the system back button to trigger the logout dialog
    override fun onBackPressed() {
        super.onBackPressed()
        // Trigger the logout dialog
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        // Check if the current fragment is the HomeFragment; if so, trigger the logout dialog
        if (currentFragment is HomeFragment) {
            showLogoutDialog() // Show logout dialog
        } else {
            super.onBackPressed() // Handle default back navigation
        }
    }


    private fun replaceFragment(fragment : Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}