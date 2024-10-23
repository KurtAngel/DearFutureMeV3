package com.example.dearfutureme.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.Activities.CreateCapsule
import com.example.dearfutureme.Activities.SharedCapsule
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.APIResponse.DeletedCapsuleResponse
import com.example.dearfutureme.R
import com.example.dearfutureme.databinding.ViewholderCapsulelistBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CapsuleAdapter(private val capsuleList: MutableList<Capsules>) : RecyclerView.Adapter<CapsuleAdapter.CapsuleViewHolder>() {

    private lateinit var context:Context


    inner class CapsuleViewHolder(val binding: ViewholderCapsulelistBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CapsuleViewHolder {

        context=parent.context
        val binding = ViewholderCapsulelistBinding.inflate(LayoutInflater.from(context), parent, false)
        return CapsuleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CapsuleViewHolder, position: Int) {
        val capsule = capsuleList[position]
        holder.binding.IvCapsule.setImageResource(R.drawable.draft_capsule)
        holder.binding.tvTitle.text = capsule.title

        holder.binding.IvCapsule.setOnClickListener{
            RetrofitInstance.instance.getCapsuleById(capsule.id).enqueue(object : Callback<Capsules>
            {
                override fun onResponse(call: Call<Capsules>, response: Response<Capsules>) {
                    if (response.isSuccessful) {
                        response.body()?.let { capsule ->
                            Log.d("CapsuleAdapter", "Received capsule: $capsule")
                            // Create Intent to navigate to the editing activity
                            val intent = Intent(holder.itemView.context, CreateCapsule::class.java).apply {
                                putExtra("MODE", "EDIT")
                                putExtra("CAPSULE", capsule)
                            }
                            holder.itemView.context.startActivity(intent)
                        }
                    } else {
                        Log.e("CapsuleAdapter", "Error: Response unsuccessful")
                    }
                }

                override fun onFailure(call: Call<Capsules>, t: Throwable) {
                    Toast.makeText(this@CapsuleAdapter.context, "Error occurred while editing the capsule", Toast.LENGTH_SHORT).show()
                    Log.e("CapsuleAdapter", "Error: ${t.message}")
                }
            })
        }

        holder.binding.shareBtn.setOnClickListener{

            RetrofitInstance.instance.getCapsuleById(capsule.id).enqueue(object : Callback<Capsules>
            {
                override fun onResponse(call: Call<Capsules>, response: Response<Capsules>) {
                    if (response.isSuccessful) {
                        response.body()?.let { capsule ->
                            Log.d("CapsuleAdapter", "Received capsule: $capsule")
                            // Create Intent to navigate to the editing activity
                            val intent = Intent(this@CapsuleAdapter.context, SharedCapsule::class.java).apply {
                                putExtra("CAPSULE", capsule)
                            }
                            holder.itemView.context.startActivity(intent)
                        }
                    } else {
                        Log.e("CapsuleAdapter", "Error: Response unsuccessful")
                    }
                }

                override fun onFailure(call: Call<Capsules>, t: Throwable) {
                    Toast.makeText(this@CapsuleAdapter.context, "Error occurred while editing the capsule", Toast.LENGTH_SHORT).show()
                    Log.e("CapsuleAdapter", "Error: ${t.message}")
                }
            })
        }

        setupListeners(holder, position ,capsule.id)

//        updateCapsuleList(capsuleList)
    }

    override fun getItemCount(): Int = capsuleList.size

    fun updateCapsuleList(newCapsuleList: List<Capsules>) {
        capsuleList.clear()
        capsuleList.addAll(newCapsuleList)
        notifyDataSetChanged()
    }

    private fun setupListeners(holder: CapsuleViewHolder, position: Int, capsuleId: Int) {
        holder.binding.deleteCapsuleBtn.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Delete Capsule")
            builder.setMessage("Are you sure you want to delete this capsule?")
            builder.setPositiveButton("Yes") { dialog, which -> deleteCapsule(position, capsuleId) }
            builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            builder.create().show()
        }
    }

    private fun deleteCapsule(position: Int, capsuleId: Int) {
            // Make a delete request using Retrofit to remove the capsule by its ID
            RetrofitInstance.instance.deleteCapsule(capsuleId).enqueue(object : Callback<DeletedCapsuleResponse> {
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
}

