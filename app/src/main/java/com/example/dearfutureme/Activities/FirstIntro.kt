package com.example.dearfutureme.Activities

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dearfutureme.databinding.ActivityFirstIntroBinding

class FirstIntro : AppCompatActivity() {

    lateinit var binding: ActivityFirstIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFirstIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val paint = binding.tvMoments.paint
        val width = paint.measureText(binding.tvMoments.text.toString())
        binding.tvMoments.paint.shader = LinearGradient(
            0f,0f,width,binding.tvMoments.textSize, intArrayOf(
                Color.parseColor("#6B26D4"),
                Color.parseColor("#C868FF")
            ), null, Shader.TileMode.CLAMP
        )

        binding.main.setOnClickListener{
            startActivity(Intent(this@FirstIntro, SecondIntro::class.java))
            finish()
        }
    }
}