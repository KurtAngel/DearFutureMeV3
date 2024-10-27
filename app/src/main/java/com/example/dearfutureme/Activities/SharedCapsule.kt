package com.example.dearfutureme.Activities

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.DataRepository.SharingCapsuleRepository
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.Model.Image
import com.example.dearfutureme.R
import com.example.dearfutureme.databinding.ActivitySharedCapsuleBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SharedCapsule : AppCompatActivity() {

    private lateinit var binding: ActivitySharedCapsuleBinding
    private val imagesList = mutableListOf<Image>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedCapsuleBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        bundleData()
        backBtn()
        setGradient()
        sendBtn()

        val capsule = intent.getParcelableExtra<Capsules>("CAPSULE")
        Log.d("CapsuleAdapter", capsule.toString())
        Log.d("CapsuleAdapter", capsule?.images.toString())
        Log.d("CapsuleAdapterUrl", SharingCapsuleRepository.capsuleImages.toString())
    }

    private fun sendBtn() {
        binding.sendBtn.setOnClickListener {
            val capsule = intent.getParcelableExtra<Capsules>("CAPSULE")
            val (dateSet, timeSet) = capsule?.scheduledOpenAt.toString().split(" ")
            val title = capsule?.title.toString()
            val message = capsule?.message.toString()
            val date = dateSet
            val time = timeSet
            val receiverEmail = capsule?.receiverEmail.toString()

            // Prepare text fields as RequestBody
            val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val messagePart = message.toRequestBody("text/plain".toMediaTypeOrNull())
            val datePart = "$date $time".toRequestBody("text/plain".toMediaTypeOrNull())
            val receiverEmailPart = receiverEmail.toRequestBody("text/plain".toMediaTypeOrNull())

            // Prepare image file (if you have an image)
            val imageParts = mutableListOf<MultipartBody.Part>()
            for (imageFile in imagesList) {
                val file = File(imageFile.imageUrl)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("images[]", file.name, requestFile)
                imageParts.add(imagePart)
            }

            if(title.isNotEmpty() && message.isNotEmpty() && date.isNotEmpty()) {
                Log.d("CreateCapsule", "Title: $title, Message: $message, Date: $date, Time: $time")

                RetrofitInstance.instance.createCapsule(
                    images = imageParts,
                    title = titlePart,
                    message = messagePart,
                    receiverEmail = receiverEmailPart,
                    scheduledOpenAt = datePart
                ).enqueue(object : Callback<Capsules> {

                    override fun onResponse(call: Call<Capsules>, response: Response<Capsules>) {
                        Log.d("UploadResponse", "Code: ${response.code()}, Body: ${response.body()}")
                        if (response.isSuccessful && response.body() != null) {
                            Log.d("UploadResponse", "Code: ${response.code()}, Body: ${response.body()}")
                            Toast.makeText(this@SharedCapsule, "Capsule shared Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SharedCapsule, MyCapsuleList::class.java)
                            startActivity(intent)
                            finish()
                            response.message()
                        } else {
                            Toast.makeText(this@SharedCapsule, "Email address doesn't exist.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Capsules>, t: Throwable) {
                        Toast.makeText(this@SharedCapsule, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Enter some message", Toast.LENGTH_SHORT).show()
            }
        }
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