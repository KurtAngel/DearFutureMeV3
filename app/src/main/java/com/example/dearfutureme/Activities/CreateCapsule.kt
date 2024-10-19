package com.example.dearfutureme.Activities

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.R
import com.example.dearfutureme.databinding.ActivityCreateCapsuleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class CreateCapsule : AppCompatActivity() {

    lateinit var binding: ActivityCreateCapsuleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCapsuleBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        sendBtn()
        draftBtn()
        backBtn()
        setDate()
    }

    private fun setDate() {
        val editTextDate = binding.dateSchedule
        editTextDate.setOnClickListener {
            // Get current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Show DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    // Format date as dd/MM/yyyy
                    val selectedDate = "${monthOfYear + 1}/$dayOfMonth/$year"
                    editTextDate.setText(selectedDate)  // Set date to EditText
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }

    //    private fun backBtn() {
//        binding.btnBack.setOnClickListener {
//            startActivity(Intent(this@CreateCapsule, MyCapsuleList::class.java))
//        }
//    }
    private fun backBtn() {
        binding.btnBack.setOnClickListener {
            val intent = Intent()
            intent.putExtra("USERNAME", intent.getStringExtra("USERNAME")) // Pass the username back
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun draftBtn() {
        binding.draftBtn.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val message = binding.etMessage.text.toString()
            val date = binding.dateSchedule.text.toString()

            val imageResId = getImageResIdForCapsule()

            if(title.isNotEmpty() && message.isNotEmpty() && date.isNotEmpty()) {
                val request = Capsules(0, title, message, null, null, null, null)
                RetrofitInstance.instance.createCapsule(request).enqueue(object :
                    Callback<Capsules> {
                    override fun onResponse(call: Call<Capsules>, response: Response<Capsules>) {
                        if (response.isSuccessful && response.body() != null) {
                            val capsule = response.body()?.draft
                            Toast.makeText(this@CreateCapsule, capsule, Toast.LENGTH_SHORT).show()
                            val intent = Intent()
                            intent.putExtra("USERNAME", intent.getStringExtra("USERNAME")) // Pass the username back
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Capsules>, t: Throwable) {
                        Toast.makeText(this@CreateCapsule, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun getImageResIdForCapsule(): Bitmap {
        val options = BitmapFactory.Options().apply {
            inSampleSize = 2 // Scale the image by half (or higher for more compression)
        }
        return BitmapFactory.decodeResource(resources, R.drawable.capsule, options)
    }

    private fun sendBtn() {
        binding.sendBtn.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val message = binding.etMessage.text.toString()
            val date = binding.dateSchedule.text.toString()
            val schedule = binding.dateSchedule.text.toString()
//            val receiverEmail = binding.etReceiverEmail.text.toString()

        }
    }
}