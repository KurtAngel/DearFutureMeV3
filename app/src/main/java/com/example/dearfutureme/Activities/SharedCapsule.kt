package com.example.dearfutureme.Activities

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.R
import com.example.dearfutureme.databinding.ActivitySharedCapsuleBinding

class SharedCapsule : AppCompatActivity() {

    private lateinit var binding: ActivitySharedCapsuleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedCapsuleBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        bundleData()
        backBtn()
        setGradient()
    }

    private fun backBtn() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun bundleData() {
        binding.apply {
            val capsule = intent.getParcelableExtra<Capsules>("CAPSULE")
            tvTitle.text = capsule?.title
            val (date, time) = capsule?.scheduledOpenAt.toString().split("")
            tvDataTime.text = date

        }
    }
    private fun setGradient() {
        val paint = binding.tvDearFutureMe.paint
        val width = paint.measureText(binding.tvDearFutureMe.text.toString())
        binding.tvDearFutureMe.paint.shader = LinearGradient(
            0f,0f,width,binding.tvDearFutureMe.textSize, intArrayOf(
                Color.parseColor("#6B26D4"),
                Color.parseColor("#C868FF")
            ), null, Shader.TileMode.CLAMP
        )
    }
}