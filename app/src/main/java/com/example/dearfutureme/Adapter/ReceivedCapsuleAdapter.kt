package com.example.dearfutureme.Adapter

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.APIResponse.DeletedCapsuleResponse
import com.example.dearfutureme.Activities.ReceivedCapsuleView
import com.example.dearfutureme.DataRepository.CapsuleCount
import com.example.dearfutureme.DataRepository.ImageRepository
import com.example.dearfutureme.DataRepository.SenderRepository
import com.example.dearfutureme.Model.Image
import com.example.dearfutureme.Model.ReceivedCapsule
import com.example.dearfutureme.R
import com.example.dearfutureme.databinding.ViewholderReceivedCapsuleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReceivedCapsuleAdapter(private val capsuleList: MutableList<ReceivedCapsule>) : RecyclerView.Adapter<ReceivedCapsuleAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(val binding: ViewholderReceivedCapsuleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(capsule: ReceivedCapsule) {
            // Set the title and capsule image
            binding.tvTitle.text = capsule.title
            binding.IvReceivedCapsule.setImageResource(R.drawable.pink_capsule)

            // Update the open timer
            startCountdownTimer(capsule.scheduledOpenAt)

            // Set click listener
            binding.IvReceivedCapsule.setOnClickListener {
                if (isCapsuleReady(capsule.scheduledOpenAt)) {
                    RetrofitInstance.instance.getReceivedCapsuleById(capsule.id).enqueue(object : Callback<ReceivedCapsule> {
                        override fun onResponse(call: Call<ReceivedCapsule>, response: Response<ReceivedCapsule>) {
                            if (response.isSuccessful) {
                                response.body()?.let { capsules ->

                                    // Log the entire capsule object to confirm it's correct
                                    Log.d("Capsule", capsules.toString())

                                    // Access the images list
                                    val images = capsules.images

                                    // Log the images list to ensure it's not null
                                    Log.d("Images", images.toString())

                                    // Check if images are not null and have valid URLs
                                    images?.forEach { image ->
                                        Log.d("Image URL", image.imageUrl ?: "Image URL is null")
                                    }
                                    val imageUrls = images?.map {
                                        it.imageUrl
                                    }
                                    ImageRepository.imageGetter = imageUrls
                                    SenderRepository.senderGetter = capsules.sender

                                    val intent = Intent(context, ReceivedCapsuleView::class.java).apply {
                                        putExtra("CAPSULE", capsules)
                                    }
                                    context.startActivity(intent)
                                }
                            } else {
                                Log.e("CapsuleAdapter", "Error: Response unsuccessful")
                            }
                        }

                        override fun onFailure(call: Call<ReceivedCapsule>, t: Throwable) {
                            Log.e("CapsuleAdapter", "Error: ${t.message}")
                        }
                    })
                } else {
                    Toast.makeText(context, "This capsule is not ready yet.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun startCountdownTimer(scheduledOpenAt: String) {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val openDate: Date? = formatter.parse(scheduledOpenAt)
            val currentTime = Date()

            if (openDate != null) {
                val remainingTime = openDate.time - currentTime.time

                if (remainingTime > 0) {
                    // Initialize the countdown timer
                    object : CountDownTimer(remainingTime, 1000) { // Count down every second (1000 ms)
                        override fun onTick(millisUntilFinished: Long) {
                            val secondsLeft = (millisUntilFinished / 1000).toInt()
                            val minutesLeft = (millisUntilFinished / (1000 * 60) % 60).toInt()
                            val hoursLeft = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
                            val daysLeft = (millisUntilFinished / (1000 * 60 * 60 * 24)).toInt()

                            val timeString = when {
                                // Show days with hours if more than 1 day is left
                                daysLeft > 0 -> "$daysLeft days, $hoursLeft hours"
                                // Show hours with minutes if less than a day but more than 1 hour left
                                hoursLeft > 0 -> "$hoursLeft hours, $minutesLeft minutes"
                                // Show minutes with seconds if less than an hour but more than 1 minute left
                                minutesLeft > 0 -> "$minutesLeft minutes, $secondsLeft seconds"
                                // Show only seconds if less than a minute is left
                                else -> "$secondsLeft seconds"
                            }

                            // Update the UI every second
                            binding.tvOpenTimer.text = timeString
                        }

                        override fun onFinish() {
                            // Timer is done, update the UI to indicate that
                            binding.tvOpenTimer.text = "Ready!"
                        }
                    }.start() // Start the countdown
                } else {
                    // If the time has already passed
                    binding.tvOpenTimer.text = "Ready!"
                }
            } else {
                binding.tvOpenTimer.text = "Invalid date"
            }
        }



        private fun isCapsuleReady(scheduledOpenAt: String): Boolean {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val openDate: Date? = formatter.parse(scheduledOpenAt)
            val currentTime = Date()
            return openDate != null && openDate <= currentTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderReceivedCapsuleBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val capsule = capsuleList[position]
        CapsuleCount.sentCapsule = CapsuleCount.sentCapsule!! + 1
        holder.bind(capsule)

        holder.binding.deleteCapsuleBtn.setOnClickListener {
            setupListeners(holder, position, capsule.id)
        }

    }

    private fun setupListeners(holder: ReceivedCapsuleAdapter.ViewHolder, position: Int, capsuleId: Int) {
            // Get the layoutInflater from the context
            val layoutInflater = LayoutInflater.from(holder.itemView.context)

            // Inflate the custom dialog layout
            val customView = layoutInflater.inflate(R.layout.custom_delete_dialog, null)

            // Build the AlertDialog
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setView(customView)

            // Create and show the dialog
            val dialog = builder.create()

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            // Set up custom dialog's UI elements
            val yesButton = customView.findViewById<Button>(R.id.btnYes)
            val noButton = customView.findViewById<Button>(R.id.btnNo)

            yesButton.setOnClickListener {
                deleteCapsule(position, capsuleId)
                dialog.dismiss()
            }

            noButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

    private fun deleteCapsule(position: Int, capsuleId: Int) {

                // Make a delete request using Retrofit to remove the capsule by its ID
        RetrofitInstance.instance.deleteReceivedCapsule(capsuleId).enqueue(object : Callback<DeletedCapsuleResponse> {
            override fun onResponse(call: Call<DeletedCapsuleResponse>, response: Response<DeletedCapsuleResponse>) {
                if (response.isSuccessful) {
                    // Capsule deleted successfully
                    val deleteResponse = response.body()?.message
                    Toast.makeText(context, "$deleteResponse", Toast.LENGTH_SHORT).show()

                    // Remove the capsule from the list and notify the adapter
                    capsuleList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, capsuleList.size)
                } else {
                    // Handle the error when the response is not successful
                    Toast.makeText(context, "Failed to delete the capsule", Toast.LENGTH_SHORT).show()
                    Log.e("CapsuleAdapter", "Error: Failed to delete capsule - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DeletedCapsuleResponse>, t: Throwable) {
                // Handle the failure
                Toast.makeText(context, "Error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("CapsuleAdapter", "Error: ${t.message}")
            }
        })
    }


    fun countSentCapsules(): Int {
        return itemCount
    }

    fun updateCapsuleList(newCapsules: List<ReceivedCapsule>) {
        capsuleList.clear()
        capsuleList.addAll(newCapsules)
        notifyDataSetChanged()  // Notify that the data has changed
    }

    override fun getItemCount(): Int = capsuleList.size
}