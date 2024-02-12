package com.speakwithai.basestructure.ui.wallpaper.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.corylustech.superai.presentation.wallpaper.model.ColorModel
import com.speakwithai.basestructure.R

class TonesAdapter(private val mList: List<ColorModel>) : RecyclerView.Adapter<TonesAdapter.ViewHolder>() {

    private var clickListener: TonsClickListener? = null

    fun setClickListener(listener: TonsClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        holder.imageView.setBackgroundColor(Color.parseColor(ItemsViewModel.colocode))
        holder.imageView.setOnClickListener{
            val q=ItemsViewModel.coloname.toString()
            clickListener?.tonsItemClick(q)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.colorimage)
    }
}