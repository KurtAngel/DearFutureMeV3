package com.example.dearfutureme.Activities

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
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
            val (date, time) = capsule?.scheduledOpenAt.toString().split(" ")
            Log.d("date and time", "$date and $time")
            tvDate.text = date
            tvTime.text = time
            etEmailAddress.setText(capsule?.receiverEmail)
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

        val paint2 = binding.textView20.paint
        val width2 = paint2.measureText(binding.textView20.text.toString())
        binding.textView20.paint.shader = LinearGradient(
            0f,0f,width2,binding.textView20.textSize, intArrayOf(
                Color.parseColor("#F25597"),
                Color.parseColor("#F25597")
            ), null, Shader.TileMode.CLAMP
        )

        val paint3 = binding.textView21.paint
        val width3 = paint3.measureText(binding.textView21.text.toString())
        binding.textView21.paint.shader = LinearGradient(
            0f,0f,width3,binding.textView21.textSize, intArrayOf(
                Color.parseColor("#F25597"),
                Color.parseColor("#F25597")
            ), null, Shader.TileMode.CLAMP
        )
    }
}