package com.example.dearfutureme.Activities

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dearfutureme.databinding.ActivityThirdIntroBinding

class ThirdIntro : AppCompatActivity() {

    lateinit var binding: ActivityThirdIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityThirdIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val paint = binding.tvRelieve.paint
        val width = paint.measureText(binding.tvRelieve.text.toString())
        binding.tvRelieve.paint.shader = LinearGradient(
            0f,0f,width,binding.tvRelieve.textSize, intArrayOf(
                Color.parseColor("#6B26D4"),
                Color.parseColor("#C868FF")
            ), null, Shader.TileMode.CLAMP
        )

        binding.btnGetStarted.setOnClickListener{
            startActivity(Intent(this@ThirdIntro, MainActivity::class.java))
            finish()
        }

    }
}