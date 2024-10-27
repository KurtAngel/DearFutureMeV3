package com.example.dearfutureme.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dearfutureme.databinding.ViewholderReceivedImagesBinding

class ReceivedImageAdapter(private val imageUrls: List<String?>) : RecyclerView.Adapter<ReceivedImageAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderReceivedImagesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            // Load image using Glide or any other image loading library
            Glide.with(itemView.context)
                .load(imageUrl) // Here, you are loading the image URL directly
                .into(binding.receivedImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderReceivedImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        if (imageUrl != null) {
            holder.bind(imageUrl)
        }
    }

    override fun getItemCount(): Int = imageUrls.size
}
