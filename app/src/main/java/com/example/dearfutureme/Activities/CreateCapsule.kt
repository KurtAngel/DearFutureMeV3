package com.example.dearfutureme.Activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.Model.EditCapsuleResponse
import com.example.dearfutureme.R
import com.example.dearfutureme.databinding.ActivityCreateCapsuleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class CreateCapsule : AppCompatActivity() {

    lateinit var binding: ActivityCreateCapsuleBinding
    private lateinit var mode: String
    var capsule: Capsules? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCapsuleBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        mode = intent.getStringExtra("MODE").toString()
        capsule = intent.getParcelableExtra("CAPSULE")

        sendBtn()
        backBtn()
        setDate()
        setTime()

        if(mode == "EDIT"){
            editBtn()
        } else {
            createBtn()
        }
    }

    private fun setTime() {
        val timeEditText: EditText = binding.timeSchedule

        timeEditText.setOnClickListener {
            // Get the current time
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)

            // Create and show TimePickerDialog
            val timePickerDialog = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    // Format and set the selected time in HH:MM format
                    val formattedTime = String.format("%02d : %02d : %02d", selectedHour, selectedMinute, second)
                    timeEditText.setText(formattedTime) // Set the time in EditText
                },
                hour, minute, true // true for 24-hour format
            )

            timePickerDialog.show() // Show the time picker dialog
        }
    }

    private fun editBtn() {
        binding.tvMyCapsule.text = "Edit Capsule"
        val title =binding.etTitle.setText(capsule?.title).toString()
        val message = binding.etMessage.setText(capsule?.message).toString()
        val receiver_email = binding.receiverEmail.setText(capsule?.receiverEmail).toString()


        binding.draftBtn.setOnClickListener{
            val request = capsule?.let { it1 -> Capsules(it1.id, title, message, null, receiver_email, null, null, null) }
            if (request != null) {
                capsule?.let { it1 ->
                    RetrofitInstance.instance.updateCapsule(it1.id, request).enqueue(object : Callback<Capsules>{
                        override fun onResponse(call: Call<Capsules>, response: Response<Capsules>) {
//                            val editResponse = response.body()?.updateMessage
                            if(response.isSuccessful){
                                val capsule = response.body()?.title
                                Log.d("CapsuleUpdate", "Capsule updated successfully: $capsule")
    //                            Toast.makeText(this@CreateCapsule, editResponse, Toast.LENGTH_SHORT).show()
                                displayName()
                            } else {
                                Log.e("CapsuleUpdate", "Error: Response unsuccessful")
                            }
                        }

                        override fun onFailure(call: Call<Capsules>, t: Throwable) {
                            Log.e("Update Error", "Error: ${t.message}")
                        }
                    })
                }
            }
        }
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
                    val selectedDate = "$year / ${monthOfYear + 1} / $dayOfMonth"
                    editTextDate.setText(selectedDate)  // Set date to EditText
                },
                year, month, day
            )
            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            datePickerDialog.show()
        }
    }

    private fun backBtn() {
        binding.btnBack.setOnClickListener {
            val intent = Intent()
            intent.putExtra("USERNAME", intent.getStringExtra("USERNAME")) // Pass the username back
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun createBtn() {
        binding.draftBtn.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val message = binding.etMessage.text.toString()
            val date = binding.dateSchedule.text.toString()
            val time = binding.timeSchedule.text.toString()
            val receiver_email = binding.receiverEmail.text.toString()

            if(title.isNotEmpty() && message.isNotEmpty() && date.isNotEmpty()) {
                Log.d("CreateCapsule", "Title: $title, Message: $message, Date: $date, Time: $time")
                val request = Capsules(0, title, message, null, receiver_email, "$date $time", null, null)
                RetrofitInstance.instance.createCapsule(request).enqueue(object : Callback<Capsules> {
                    override fun onResponse(call: Call<Capsules>, response: Response<Capsules>) {
                        if (response.isSuccessful && response.body() != null) {
                            val capsule = response.body()?.draft
                            Toast.makeText(this@CreateCapsule, capsule, Toast.LENGTH_SHORT).show()
                            displayName()
                        }
                    }
                    override fun onFailure(call: Call<Capsules>, t: Throwable) {
                        Toast.makeText(this@CreateCapsule, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Put something Bitch!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayName() {
        val intent = Intent()
        intent.putExtra("USERNAME", intent.getStringExtra("USERNAME")) // Pass the username back
        setResult(RESULT_OK, intent)
        finish()
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