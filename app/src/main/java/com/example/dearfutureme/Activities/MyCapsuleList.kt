package com.example.dearfutureme.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.API.TokenManager
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.R
import com.example.dearfutureme.ViewModel.MainViewModel
import com.example.dearfutureme.databinding.ActivityMyCapsuleListBinding
import com.example.dearfutureme.fragments.HomeFragment
import com.example.dearfutureme.fragments.ShareCapsuleFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MyCapsuleList : AppCompatActivity() {

    private lateinit var binding: ActivityMyCapsuleListBinding

    lateinit var tokenManager: TokenManager
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMyCapsuleListBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        tokenManager = TokenManager(this)
        RetrofitInstance.init(this)


        replaceFragment(HomeFragment())

        setUpListeners()
    }



    private fun setUpListeners() {
        binding.addCapsuleBtn.setOnClickListener {
            startActivity(Intent(this@MyCapsuleList, CreateCapsule::class.java))
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
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
}