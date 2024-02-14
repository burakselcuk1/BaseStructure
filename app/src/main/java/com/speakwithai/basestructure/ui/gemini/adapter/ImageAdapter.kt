package com.speakwithai.basestructure.ui.gemini.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.speakwithai.basestructure.databinding.AdapterImageBinding

class ImageAdapter(private val onDeleteClickListener: (position: Int) -> Unit) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val imageList = mutableListOf<Bitmap?>()

    fun setImageList(imageList: List<Bitmap?>) {
        this.imageList.apply {
            clear()
            addAll(imageList)
        }
        notifyDataSetChanged()
    }

    class ImageViewHolder(private val binding: AdapterImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bitmap: Bitmap?, onDeleteClickListener: (position: Int) -> Unit) {
            if (bitmap != null) {
                binding.adapterImage.setImageBitmap(bitmap)
            } else {
                binding.adapterImage.setImageBitmap(null)
            }

            binding.delete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener(position)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            AdapterImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        holder.bind(imageList[position]) { pos ->
            // Delete button click callback
            onDeleteClickListener(pos)
        }
    }
}

