package com.example.dearfutureme.Activities

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.APIResponse.UploadResponse
import com.example.dearfutureme.Adapter.ReceivedCapsuleAdapter
import com.example.dearfutureme.DataRepository.CapsuleCount
import com.example.dearfutureme.DataRepository.ProfileRepository
import com.example.dearfutureme.DataRepository.UserRepository
import com.example.dearfutureme.R
import com.example.dearfutureme.ViewModel.MainViewModel
import com.example.dearfutureme.databinding.ActivityProfileSettingsBinding
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import java.io.File
import java.io.FileInputStream

import retrofit2.Callback
import retrofit2.Response

class ProfileSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSettingsBinding
    private var selectedImageUri: Uri? = null
    private var viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListeners()
        bundle()
    }

    private fun bundle() {
        binding.apply {
            val username = UserRepository.username
            val email = UserRepository.email
            val imageHolder = ProfileRepository.imageHolder
            val draftCapsuleCount = CapsuleCount.draftCapsule
            val sentCapsuleCount = CapsuleCount.sentCapsule
            tvUsername.text = username.toString()
            tvEmail.text = email.toString()
            tvDraftCapsuleCount.text = draftCapsuleCount.toString()
            tvSentCapsuleCount.text = sentCapsuleCount.toString()
            imageHolder?.let {
                imageProfile.setImageURI(it)
            }
        }
        initProfilePic()
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
               View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    private fun setUpListeners() {
        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MyCapsuleList::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        binding.uploadProfile.setOnClickListener {
            pickImage()
        }
    }

    private fun pickImage() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data

            binding.imageProfile.setImageURI(selectedImageUri)
            ProfileRepository.imageHolder = selectedImageUri
            selectedImageUri?.let {
                uploadImageToServer(it)
            }
        }
    }

    private fun uploadImageToServer(imageUri: Uri) {
        Log.d("Image Uri", imageUri.toString())
        val parcelFileDescriptor = contentResolver.openFileDescriptor(imageUri, "r", null) ?: return
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(imageUri))
        val outputStream = file.outputStream()
        inputStream.copyTo(outputStream)

        // Prepare the image part
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("profile_pic", file.name, requestFile)

        // Make the network request
        RetrofitInstance.instance.uploadProfileImage(body).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    Log.d("Upload", "Success: ${response.body()}")
                } else {
                    Log.e("Upload", "Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.e("Upload", "Error: ${t.message}")
            }
        })
    }

    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    private fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val returnCursor = query(uri, null, null, null, null)
        returnCursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst()) name = it.getString(nameIndex)
        }
        return name
    }

    private fun initProfilePic() {
        // Use viewLifecycleOwner to observe LiveData
        viewModel.imageGetter.observe(this, Observer {

            Glide.with(this)
                .load(ProfileRepository.imageGetter)
                .placeholder(R.drawable.baseline_person_24)
                .error(R.drawable.baseline_person_24)
                .into(binding.imageProfile)
        })
        viewModel.loadProfilePic()
    }
}
