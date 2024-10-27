package com.example.dearfutureme.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dearfutureme.Model.Image
import com.example.dearfutureme.R
import com.example.dearfutureme.databinding.ViewholderImageBinding

class ImageAdapter(private val imageList: MutableList<Image>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(val binding: ViewholderImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Image) {
            // Load image using Glide or any other image loading library
            Glide.with(itemView.context)
                .load(image.imageUrl)
                .into(binding.IvImageHolder)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ViewholderImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imageList[position]
        holder.bind(image)
        holder.itemView.setOnClickListener {
            Log.d("ImageAdapter", "Item clicked: $position")

            holder.binding.removeImage.setOnClickListener {

                removeItem(position)
            }
        }
    }

    // Method to remove an item from the list
    private fun removeItem(position: Int) {
        imageList.removeAt(position)
        notifyItemRemoved(position)  // Notify the adapter that an item was removed
        notifyItemRangeChanged(position, imageList.size)  // Update the remaining items
    }

    override fun getItemCount(): Int = imageList.size
}
