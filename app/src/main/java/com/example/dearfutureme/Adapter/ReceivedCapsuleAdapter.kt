package com.example.dearfutureme.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.Activities.ReceivedCapsuleView
import com.example.dearfutureme.DataRepository.CapsuleCount
import com.example.dearfutureme.Model.ReceivedCapsule
import com.example.dearfutureme.R
import com.example.dearfutureme.databinding.ViewholderReceivedCapsuleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceivedCapsuleAdapter(private val capsuleList: MutableList<ReceivedCapsule>) : RecyclerView.Adapter<ReceivedCapsuleAdapter.ViewHolder>()
{
    private lateinit var context: Context

    inner class ViewHolder (val binding: ViewholderReceivedCapsuleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val binding = ViewholderReceivedCapsuleBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val capsule = capsuleList[position]
        CapsuleCount.sentCapsule = CapsuleCount.sentCapsule!! + 1
        holder.binding.IvReceivedCapsule.setImageResource(R.drawable.pink_capsule)
        holder.binding.tvTitle.text = capsule.title

        holder.binding.IvReceivedCapsule.setOnClickListener {
            RetrofitInstance.instance.getReceivedCapsuleById(capsule.id).enqueue(object : Callback<ReceivedCapsule> {
                override fun onResponse(call: Call<ReceivedCapsule>, response: Response<ReceivedCapsule>)
                {
                    if (response.isSuccessful) {
                        response.body()?.let { capsules ->
                            Log.d("CapsuleAdapter", "Received capsule: $capsules")
                            // Create Intent to navigate to the editing activity
                            val intent = Intent(this@ReceivedCapsuleAdapter.context, ReceivedCapsuleView::class.java).apply {
                                putExtra("CAPSULE", capsules)
                            }
                            holder.itemView.context.startActivity(intent)
                        }
                    } else {
                        Log.e("CapsuleAdapter", "Error: Response unsuccessful")
                    }
                }

                override fun onFailure(call: Call<ReceivedCapsule>, t: Throwable) {
                    Log.e("CapsuleAdapter", "Error: ${t.message}")
                }
            })
        }
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