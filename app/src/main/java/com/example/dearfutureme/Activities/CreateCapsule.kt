package com.example.dearfutureme.Activities

import UserListAdapter
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.API.UserDataCallback
import com.example.dearfutureme.Adapter.ImageAdapter
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.Model.Image
import com.example.dearfutureme.Model.Users
import com.example.dearfutureme.R
import com.example.dearfutureme.databinding.ActivityCreateCapsuleBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
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
    private var imageAdapter = ImageAdapter(mutableListOf())
    private val imagesList = mutableListOf<Image>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCapsuleBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        mode = intent.getStringExtra("MODE").toString()
        capsule = intent.getParcelableExtra("CAPSULE")
        Log.d("Image upload", "$imagesList")

        initImageRecycler()
        sendBtn()
        backBtn()
        setDate()
        setTime()
        addImage()

        if(mode == "EDIT"){
            editBtn()
        } else {
            createBtn()
        }
        hideNavigationBar()

        binding.receiverEmail.setOnClickListener {
            showUserListDialog(binding.receiverEmail)
        }
    }

    // Function to fetch user data (simulate a database fetch)
    private fun fetchUserData(callback: UserDataCallback) {
        RetrofitInstance.instance.getAllUsers().enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val userList = response.body()?.data?.map { it.email } ?: emptyList()
                    Log.d("UserList", "User List: $userList")
                    // Pass the fetched data to the callback
                    callback.onUserDataFetched(userList)
                } else {
                    callback.onError("Failed to fetch user data")
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Log.e("UserList", "Error: ${t.message}")
                // Pass the error message to the callback
                callback.onError("Error: ${t.message}")
            }
        })
    }

    //     Show a dialog with the list of users
    private fun showUserListDialog(editText: EditText) {
        fetchUserData(object : UserDataCallback {
            override fun onUserDataFetched(userList: List<String>) {
                // Show the dialog once data is fetched
                val dialogView = layoutInflater.inflate(R.layout.dialog_user_list, null)
                val userListView = dialogView.findViewById<ListView>(R.id.userListView)

                val adapter = UserListAdapter(this@CreateCapsule, userList)
                userListView.adapter = adapter

                val dialog = AlertDialog.Builder(this@CreateCapsule)
                    .setView(dialogView)
                    .create()

                userListView.setOnItemClickListener { _, _, position, _ ->
                    val selectedUser = userList[position]
                    editText.setText(selectedUser)
                    dialog.dismiss()
                }
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.show()
            }

            override fun onError(errorMessage: String) {
                // Handle error (e.g., show a Toast or Log)
                Toast.makeText(this@CreateCapsule, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun initImageRecycler() {
        imageAdapter = ImageAdapter(imagesList) // Use your adapter that displays image filenames
        binding.recyclerViewImage.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerViewImage.adapter = imageAdapter
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
                        imageUrl = imageFile.absolutePath
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
                    imageUrl = imageUrl.toString()
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
//                                displayName()
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
                            Toast.makeText(this@CreateCapsule, "Capsule has been sent Successfully", Toast.LENGTH_SHORT).show()
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

//class CreateCapsule : AppCompatActivity() {
//
//    lateinit var binding: ActivityCreateCapsuleBinding
//    private lateinit var mode: String
//    var capsule: Capsules? = null
//    private lateinit var imageAdapter: ImageAdapter
//    private val imagesList = mutableListOf<Image>()
//    private var username: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityCreateCapsuleBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
//        setContentView(binding.root)
//
//        imageAdapter = ImageAdapter(imagesList)
//        binding.recyclerViewImage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.recyclerViewImage.adapter = imageAdapter
//        mode = intent.getStringExtra("MODE").toString()
//        capsule = intent.getParcelableExtra("CAPSULE")
//
//        sendBtn()
//        backBtn()
//        setDate()
//        setTime()
//        addImage()
//
//        username = intent.getStringExtra("USERNAME") ?: "GUEST"
//
//        if (mode == "EDIT") {
//            Log.d("Edit Capsule", "Capsule: $capsule")
//            editBtn()
//        } else {
//            createBtn()
//        }
//        hideNavigationBar()
//    }
//
//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        if (hasFocus) {
//            hideNavigationBar()
//        }
//    }
//
//    private fun hideNavigationBar() {
//        val decorView = window.decorView
//        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//    }
//
//    private fun addImage() {
//        binding.addImageBtn.setOnClickListener {
//            pickImageFromGallery()
//        }
//    }
//
//    private val IMAGE_PICK_CODE = 1001
//
//    private fun pickImageFromGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, IMAGE_PICK_CODE)
//    }
//
//    private fun getFileName(uri: Uri): String? {
//        val cursor = contentResolver.query(uri, null, null, null, null)
//        cursor?.use {
//            val nameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
//            if (it.moveToFirst()) {
//                return it.getString(nameIndex)
//            }
//        }
//        return null
//    }
//
//    @Deprecated("This method has been deprecated in favor of using the Activity Result API")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
//            val selectedImageUri = data?.data
//            if (selectedImageUri != null) {
//                val fileName = getFileName(selectedImageUri) ?: "image.jpg"
//                Log.d("Image Selection", "File Name: $fileName")
//
//                // Assuming the image is saved to the server and you construct the URL here
//                val imageUrl = "http://192.168.1.3:8000/storage/images/$fileName"
//
//                val image = Image(
//                    id = 0,
//                    imageUrl = imageUrl
//                )
//
//                imagesList.add(image)
//                imageAdapter.notifyItemInserted(imagesList.size - 1)
//            }
//        }
//    }
//
//    private fun setTime() {
//        val timeEditText: EditText = binding.timeSchedule
//
//        timeEditText.setOnClickListener {
//            val calendar = Calendar.getInstance()
//            val hour = calendar.get(Calendar.HOUR_OF_DAY)
//            val minute = calendar.get(Calendar.MINUTE)
//
//            val timePickerDialog = TimePickerDialog(
//                this,
//                { _, selectedHour, selectedMinute ->
//                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
//                    timeEditText.setText(formattedTime)
//                },
//                hour, minute, true
//            )
//
//            timePickerDialog.show()
//        }
//    }
//
//    private fun editBtn() {
//        binding.tvMyCapsule.text = "Edit Capsule"
//        binding.etTitle.setText(capsule?.title)
//        binding.etMessage.setText(capsule?.message)
//        binding.receiverEmail.setText(capsule?.receiverEmail)
//        val split = capsule?.scheduledOpenAt.toString().split(" ")
//        if (split.size == 2) {
//            val (date, time) = split
//            binding.dateSchedule.setText(date)
//            binding.timeSchedule.setText(time)
//        }
//
//        capsule?.images?.let { imageUrls ->
//            imagesList.clear() // Clear previous images to avoid duplication
//            for (imageUrl in imageUrls) {
//                val image = Image(
//                    id = 0,
//                    imageUrl = imageUrl.toString()
//                )
//                imagesList.add(image)
//            }
//            imageAdapter.notifyDataSetChanged() // Notify adapter about data change
//            Log.d("Edit Capsule", "Loaded images: $imagesList") // Debug log
//        } ?: run {
//            Log.d("Edit Capsule", "No images found in capsule.")
//        }
//
//        binding.draftBtn.setOnClickListener {
//            val request = CapsuleUpdateResponse(
//                title = binding.etTitle.text.toString(),
//                message = binding.etMessage.text.toString(),
//                receiverEmail = binding.receiverEmail.text.toString(),
//                scheduledOpenAt = "${binding.dateSchedule.text} ${binding.timeSchedule.text}"
//            )
//            RetrofitInstance.instance.updateCapsule(capsule?.id ?: 0, request).enqueue(object : Callback<Capsules> {
//                override fun onResponse(call: Call<Capsules>, response: Response<Capsules>) {
//                    if (response.isSuccessful) {
//                        Log.d("CapsuleUpdate", "Capsule updated successfully: ${response.body()?.title}")
//                        displayName()
//                    } else {
//                        Log.e("CapsuleUpdate", "Error: Response unsuccessful")
//                    }
//                }
//
//                override fun onFailure(call: Call<Capsules>, t: Throwable) {
//                    Log.e("Update Error", "Error: ${t.message}")
//                }
//            })
//        }
//    }
//
//    private fun setDate() {
//        binding.dateSchedule.setOnClickListener {
//            val calendar = Calendar.getInstance()
//            val year = calendar.get(Calendar.YEAR)
//            val month = calendar.get(Calendar.MONTH)
//            val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//            val datePickerDialog = DatePickerDialog(
//                this,
//                { _, selectedYear, selectedMonth, selectedDay ->
//                    val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
//                    binding.dateSchedule.setText(selectedDate)
//                },
//                year, month, day
//            )
//            datePickerDialog.datePicker.minDate = calendar.timeInMillis
//            datePickerDialog.show()
//        }
//    }
//
//    private fun backBtn() {
//        binding.btnBack.setOnClickListener {
//            finish()
//        }
//    }
//
//    private fun createBtn() {
//        binding.draftBtn.setOnClickListener {
//            createOrUpdateCapsule(isDraft = true)
//        }
//    }
//
//    private fun sendBtn() {
//        binding.sendBtn.setOnClickListener {
//            createOrUpdateCapsule(isDraft = false)
//        }
//    }
//
//    private fun createOrUpdateCapsule(isDraft: Boolean) {
//        val title = binding.etTitle.text.toString()
//        val message = binding.etMessage.text.toString()
//        val date = binding.dateSchedule.text.toString()
//        val time = binding.timeSchedule.text.toString()
//        val receiverEmail = binding.receiverEmail.text.toString()
//
//        val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
//        val messagePart = message.toRequestBody("text/plain".toMediaTypeOrNull())
//        val datePart = "$date $time".toRequestBody("text/plain".toMediaTypeOrNull())
//        val receiverEmailPart = receiverEmail.toRequestBody("text/plain".toMediaTypeOrNull())
//
//        val imageParts = mutableListOf<MultipartBody.Part>()
//        for (imageFile in imagesList) {
//            val file = File(imageFile.imageUrl)
//            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//            val imagePart = MultipartBody.Part.createFormData("images[]", file.name, requestFile)
//            imageParts.add(imagePart)
//        }
//
//        if (title.isNotEmpty() && message.isNotEmpty() && date.isNotEmpty()) {
//            Log.d("CreateCapsule", "Title: $title, Message: $message, Date: $date, Time: $time")
//
//            if (isDraft) {
//                RetrofitInstance.instance.createCapsule(
//                    images = imageParts,
//                    title = titlePart,
//                    message = messagePart,
//                    receiverEmail = receiverEmailPart,
//                    scheduledOpenAt = datePart
//                ).enqueue(object : Callback<Capsules> {
//                    override fun onResponse(call: Call<Capsules>, response: Response<Capsules>) {
//                        if (response.isSuccessful && response.body() != null) {
//                            Toast.makeText(this@CreateCapsule, "Capsule created Successfully", Toast.LENGTH_SHORT).show()
//                            startActivity(Intent(this@CreateCapsule, MyCapsuleList::class.java))
//                            finish()
//                        } else {
//                            Toast.makeText(this@CreateCapsule, "Email address doesn't exist.", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Capsules>, t: Throwable) {
//                        Toast.makeText(this@CreateCapsule, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//                    }
//                })
//            } else {
//                RetrofitInstance.instance.sendCapsule(
//                    images = imageParts,
//                    title = titlePart,
//                    message = messagePart,
//                    receiverEmail = receiverEmailPart,
//                    scheduledOpenAt = datePart
//                ).enqueue(object : Callback<Capsules> {
//                    override fun onResponse(call: Call<Capsules>, response: Response<Capsules>) {
//                        if (response.isSuccessful && response.body() != null) {
//                            Toast.makeText(this@CreateCapsule, "Capsule created Successfully", Toast.LENGTH_SHORT).show()
//                            capsule?.id?.let { deleteCapsule(it) }
//                            startActivity(Intent(this@CreateCapsule, MyCapsuleList::class.java))
//                            finish()
//                        } else {
//                            Toast.makeText(this@CreateCapsule, "User does not exist", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Capsules>, t: Throwable) {
//                        Toast.makeText(this@CreateCapsule, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//                    }
//                })
//            }
//        } else {
//            Toast.makeText(this, "Enter some message", Toast.LENGTH_SHORT).show()
//        }
//    }
//    private fun deleteCapsule(capsuleId: Int) {
//        RetrofitInstance.instance.deleteCapsule(capsuleId).enqueue(object : Callback<DeletedCapsuleResponse> {
//            override fun onResponse(call: Call<DeletedCapsuleResponse>, response: Response<DeletedCapsuleResponse>) {
//                if (response.isSuccessful) {
//                    Toast.makeText(this@CreateCapsule, "Capsule deleted from drafts", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this@CreateCapsule, "Failed to delete capsule from drafts", Toast.LENGTH_SHORT).show()
//                    Log.e("Delete Error", "Failed to delete capsule: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<DeletedCapsuleResponse>, t: Throwable) {
//                Toast.makeText(this@CreateCapsule, "Error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
//                Log.e("Delete Error", "Error: ${t.message}")
//            }
//        })
//    }
//
//    private fun displayName() {
//        val intent = Intent()
//        intent.putExtra("USERNAME", username) // Pass the username back
//        setResult(RESULT_OK, intent)
//        finish()
//    }
//}