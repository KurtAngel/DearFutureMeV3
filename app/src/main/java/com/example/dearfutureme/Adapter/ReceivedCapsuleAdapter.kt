package com.example.dearfutureme.Adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dearfutureme.Model.ReceivedCapsule
import com.example.dearfutureme.databinding.ViewholderReceivedCapsuleBinding

class ReceivedCapsuleAdapter(private val capsuleList: List<ReceivedCapsule>) : RecyclerView.Adapter<ReceivedCapsuleAdapter.ViewHolder>()
{
    class ViewHolder (private val binding: ViewholderReceivedCapsuleBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}