package com.example.dearfutureme.Activities

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dearfutureme.databinding.ActivitySecondIntroBinding

class SecondIntro : AppCompatActivity() {

    lateinit var binding: ActivitySecondIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val paint = binding.tvUnlock.paint
        val width = paint.measureText(binding.tvUnlock.text.toString())
        binding.tvUnlock.paint.shader = LinearGradient(
            0f,0f,width,binding.tvUnlock.textSize, intArrayOf(
                Color.parseColor("#6B26D4"),
                Color.parseColor("#C868FF")
            ), null, Shader.TileMode.CLAMP
        )

        binding.main.setOnClickListener{
            startActivity(Intent(this@SecondIntro, ThirdIntro::class.java))
            finish()
        }
    }
}