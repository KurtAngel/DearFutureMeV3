package com.example.dearfutureme.Activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dearfutureme.Model.ReceivedCapsule
import com.example.dearfutureme.databinding.ActivityReceivedCapsuleViewerBinding

class ReceivedCapsuleView: AppCompatActivity() {

    private lateinit var binding: ActivityReceivedCapsuleViewerBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            binding = ActivityReceivedCapsuleViewerBinding.inflate(layoutInflater)
            setContentView(binding.root)


            bundle()
    }

    private fun bundle() {
        binding.apply {
            val capsule = intent.getParcelableExtra<ReceivedCapsule>("CAPSULE")
            Log.d("CapsuleAdapter", "Received capsule: $capsule")
            tvTitle.text = capsule?.title.toString()
            tvMessage.text = capsule?.message.toString()
        }
    }
}


