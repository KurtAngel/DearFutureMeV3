package com.example.dearfutureme.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.R
import com.example.dearfutureme.databinding.ViewholderCapsulelistBinding

class CapsuleAdapter(val capsuleItem: MutableList<Capsules>): RecyclerView.Adapter<CapsuleAdapter.CapsuleViewHolder>() {

    private lateinit var context: Context

    inner class CapsuleViewHolder(val binding: ViewholderCapsulelistBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CapsuleAdapter.CapsuleViewHolder {
        context=parent.context
        val binding = ViewholderCapsulelistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CapsuleViewHolder(binding)
     }


    override fun onBindViewHolder(holder: CapsuleAdapter.CapsuleViewHolder, position: Int) {
        capsuleItem[position]
            holder.binding.IvCapsule.setImageResource(R.drawable.capsule)

    }

    override fun getItemCount(): Int = capsuleItem.size
}
