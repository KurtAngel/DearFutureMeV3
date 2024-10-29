package com.example.dearfutureme.Activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.Adapter.ImageAdapter
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.Model.Image
import com.example.dearfutureme.databinding.ActivityCreateCapsuleBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Calendar

class CreateCapsule : AppCompatActivity() {

    lateinit var binding: ActivityCreateCapsuleBinding
    private lateinit var mode: String
    var capsule: Capsules? = null
    private lateinit var imageAdapter: ImageAdapter
    private val imagesList = mutableListOf<Image>()
    private var username: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCapsuleBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize RecyclerView
        imageAdapter = ImageAdapter(imagesList) // Use your adapter that displays image filenames
        binding.recyclerViewImage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewImage.adapter = imageAdapter
        mode = intent.getStringExtra("MODE").toString()
        capsule = intent.getParcelableExtra("CAPSULE")

        sendBtn()
        backBtn()
        setDate()
        setTime()
        addImage()

        username = intent.getStringExtra("USERNAME") ?: "GUEST"

        if(mode == "EDIT"){
            editBtn()
        } else {
            createBtn()
        }
        hideNavigationBar()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNavigationBar()
        }
    }

    private fun hideNavigationBar() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//              or View.SYSTEM_UI_FLAG_FULLSCREEN
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    private fun addImage() {
        binding.addImageBtn.setOnClickListener {

            pickImageFromGallery()
        }
    }
    private val IMAGE_PICK_CODE = 1001

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    // Function to get the file name from the URI
    private fun getFileName(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            if (it.moveToFirst()) {
                return it.getString(nameIndex)
            }
        }
        return null
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            if (selectedImageUri != null) {
                val fileName = getFileName(selectedImageUri)
                Log.d("Image Selection", "File Name: $fileName")

                //Save the image to the app's private directory
                val publicDir = File(getExternalFilesDir(null), "images")
                if(!publicDir.exists()) {
                    publicDir.mkdirs()
                }

                val imageFile = File(publicDir, fileName ?: "image.jpg")

                try {
                    contentResolver.openInputStream(selectedImageUri)?.use { inputStream ->
                        imageFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    Log.d("Image Save", "Image saved to: ${imageFile.absolutePath}")

                    // Create your Image object
                    val image = Image(
                        id = 0,
                        imageUrl = imageFile.absolutePath,
                        capsuleId = capsule?.id ?: 0,
                        capsuleType = "default"
                    )
                    imagesList.add(image) // Add to your image list
                    imageAdapter.notifyItemInserted(imagesList.size - 1)
                } catch (e: Exception) {
                    Log.e("Image Save Error", "Error saving image: ${e.message}")
                    Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
                }
            }
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
                    val formattedTime = String.format("%02d:%02d:%02d", selectedHour, selectedMinute, second)
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
        val receiverEmail = binding.receiverEmail.setText(capsule?.receiverEmail).toString()
        val split = capsule?.scheduledOpenAt.toString().split(" ")
        val (date, time) = split
        binding.dateSchedule.setText(date).toString()
        binding.timeSchedule.setText(time).toString()

        Log.d("Image upload", "$capsule")
//         Load the existing images
        capsule?.images?.let { imageUrls ->
            for (imageUrl in imageUrls) {
                // Create an Image object (if needed) and add it to the imagesList
                val image = Image(
                    id = 0, // use 0 or another ID value
                    imageUrl = imageUrl.toString(), // the image URL or file path
                    capsuleId = capsule?.id ?: 0,
                    capsuleType = "default"
                )
                imagesList.add(image)
            imageAdapter.notifyDataSetChanged()
            }
            // Notify the adapter that new images are available
        }
//         Load the existing images from capsule.images (which is a List<Image>)
        capsule?.images?.let { imageList ->
            for (image in imageList) {
                imagesList.add(image) // Add the Image object directly to your existing imagesList
            imageAdapter.notifyDataSetChanged()
            }
            // Notify the adapter that new images are available
        }
        binding.draftBtn.setOnClickListener{
            val request = capsule?.let { it1 -> Capsules(it1.id, title, message, receiverEmail, "$date $time", null, imagesList) }
            if (request != null) {
                capsule?.let { it1 ->
                    RetrofitInstance.instance.updateCapsule(it1.id, request).enqueue(object : Callback<Capsules>{
                        override fun onResponse(call: Call<Capsules>, response: Response<Capsules>) {

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
                    val selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
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
            finish()
        }
    }

    private fun createBtn() {
        binding.draftBtn.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val message = binding.etMessage.text.toString()
            val date = binding.dateSchedule.text.toString()
            val time = binding.timeSchedule.text.toString()
            val receiverEmail = binding.receiverEmail.text.toString()

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
            Log.d("Total Images to Upload", "${imageParts.size}")
            Log.d("Image Upload", "$imagesList")

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
                            Toast.makeText(this@CreateCapsule, "Capsule created Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@CreateCapsule, MyCapsuleList::class.java)
                            startActivity(intent)
                            finish()
                            response.message()
                        } else {
                            Toast.makeText(this@CreateCapsule, "Email address doesn't exist.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Capsules>, t: Throwable) {
                        Toast.makeText(this@CreateCapsule, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Enter some message", Toast.LENGTH_SHORT).show()
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
            val time = binding.timeSchedule.text.toString()
            val receiverEmail = binding.receiverEmail.text.toString()

            // Prepare text fields as RequestBody
            val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val messagePart = message.toRequestBody("text/plain".toMediaTypeOrNull())
            val datePart = "$date $time".toRequestBody("text/plain".toMediaTypeOrNull())
            val receiverEmailPart = receiverEmail.toRequestBody("text/plain".toMediaTypeOrNull())

            // Prepare image file (if you have an image)
            val imageParts = mutableListOf<MultipartBody.Part>()
            for (imageFile in imagesList) {
                val file = imageFile.imageUrl?.let { it1 -> File(it1) }
                val requestFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = requestFile?.let { it1 ->
                    MultipartBody.Part.createFormData("images[]",
                        file.name, it1
                    )
                }
                if (imagePart != null) {
                    imageParts.add(imagePart)
                }
            }

            if(title.isNotEmpty() && message.isNotEmpty() && date.isNotEmpty()) {
                Log.d("CreateCapsule", "Title: $title, Message: $message, Date: $date, Time: $time")

                RetrofitInstance.instance.sendCapsule(
                    images = imageParts,
                    title = titlePart,
                    message = messagePart,
                    receiverEmail = receiverEmailPart,
                    scheduledOpenAt = datePart
                ).enqueue(object : Callback<Capsules> {

                    override fun onResponse(call: Call<Capsules>, response: Response<Capsules>) {
                        Log.d("UploadResponse", "Code: ${response.code()}, Body: ${response.body()}")
                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(this@CreateCapsule, "Capsule created Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@CreateCapsule, MyCapsuleList::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<Capsules>, t: Throwable) {
                        Toast.makeText(this@CreateCapsule, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Enter some message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}