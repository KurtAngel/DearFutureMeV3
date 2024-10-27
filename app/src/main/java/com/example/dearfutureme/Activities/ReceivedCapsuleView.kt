package com.example.dearfutureme.Activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dearfutureme.Adapter.ReceivedImageAdapter
import com.example.dearfutureme.DataRepository.ImageRepository
import com.example.dearfutureme.DataRepository.SenderRepository
import com.example.dearfutureme.Model.ReceivedCapsule
import com.example.dearfutureme.Model.Sender
import com.example.dearfutureme.databinding.ActivityReceivedCapsuleViewerBinding

class ReceivedCapsuleView : AppCompatActivity() {

    private lateinit var binding: ActivityReceivedCapsuleViewerBinding
    private lateinit var capsule: ReceivedCapsule
    private lateinit var sender: Sender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReceivedCapsuleViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the capsule from the intent
        capsule = intent.getParcelableExtra("CAPSULE")!!


        // Log for debugging purposes
        Log.d("Capsule", "Received capsule: ${capsule.images}")
        Log.d("ImageRepository", "Image getter: ${ImageRepository.imageGetter}")

        // Initialize views and recycler view
        bundle()
        initImageDisplay()
    }

    private fun initImageDisplay() {
        // Setup RecyclerView with LayoutManager
        binding.recyclerViewImage.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        // Check if ImageRepository has images or capsule has images
        val imageUrls = ImageRepository.imageGetter ?: capsule.images?.map {
            it.imageUrl
        }

        // Initialize the adapter with the images
        if (imageUrls != null) {
            if (imageUrls.isNotEmpty()) {
                binding.recyclerViewImage.adapter = ReceivedImageAdapter(imageUrls)
            } else {
                Log.d("Images", "No images Found")
            }
        }
    }

    private fun bundle() {
        binding.apply {
            // Bind capsule data to views
            tvTitle.text = capsule.title
            tvMessage.text = capsule.message

            SenderRepository.senderGetter?.let {
                tvSenderName.text = it.name   // Display sender's name
            } ?: run {
                tvSenderName.text = "Unknown sender"  // Fallback in case sender is null
            }
            backBtn.setOnClickListener{
                finish()
            }
        }
    }
}
