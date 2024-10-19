package com.example.dearfutureme.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.R
import com.example.dearfutureme.databinding.FragmentHomeBinding
import com.example.dearfutureme.databinding.ViewholderCapsulelistBinding
import com.google.android.material.imageview.ShapeableImageView

//class CapsuleAdapter(val capsuleItem: MutableList<Capsules>): RecyclerView.Adapter<CapsuleAdapter.CapsuleViewHolder>() {
//
//    private lateinit var context: Context
//
//    inner class CapsuleViewHolder(val binding: ViewholderCapsulelistBinding): RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CapsuleAdapter.CapsuleViewHolder {
//        context=parent.context
//        val binding = ViewholderCapsulelistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return CapsuleViewHolder(binding)
//     }
//
//
//    override fun onBindViewHolder(holder: CapsuleAdapter.CapsuleViewHolder, position: Int) {
//        Glide.with(holder.itemView.context)
//            .load(capsuleItem[position].imageResId)
//            .into(holder.binding.IvCapsule)
//    }
//
//    override fun getItemCount(): Int = capsuleItem.size
//}

//class CapsuleAdapter(private val capsuleList: MutableList<Capsules>) :
//    RecyclerView.Adapter<CapsuleAdapter.CapsuleViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CapsuleViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.viewholder_capsulelist, parent, false)
//        return CapsuleViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: CapsuleViewHolder, position: Int) {
//        val capsule = capsuleList[position]
//        holder.imageView.setImageBitmap(capsule.imageResId)
//    }
//
//    override fun getItemCount(): Int {
//        return capsuleList.size
//    }
//
//    class CapsuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageView: ShapeableImageView = itemView.findViewById(R.id.IvCapsule)
//
//        fun bind(capsule: Capsules) {
////            titleView.text = capsule.title
//
//            // Use Glide to load image
//            Glide.with(itemView.context)
//                .load(capsule.imageResId) // Assuming `Capsules` class has an `imageUrl` property
//                .placeholder(R.drawable.capsule) // Placeholder image while loading
//                .error(R.drawable.capsule) // Error image in case of failure
//                .into(imageView)
//        }
//    }
//}

class CapsuleAdapter(private val capsuleList: MutableList<Capsules>) :
    RecyclerView.Adapter<CapsuleAdapter.CapsuleViewHolder>() {

    private lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CapsuleViewHolder {
        context=parent.context
        val binding = ViewholderCapsulelistBinding.inflate(LayoutInflater.from(context), parent, false)
        return CapsuleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CapsuleViewHolder, position: Int) {
        val capsule = capsuleList[position]
        holder.binding.tvTitle.text = capsule.title
    }

    override fun getItemCount(): Int {
        return capsuleList.size
    }

    inner class CapsuleViewHolder(val binding: ViewholderCapsulelistBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = itemView.findViewById(R.id.tvTitle)
//        val imageView: ShapeableImageView = itemView.findViewById(R.id.IvCapsule)

    }
}

