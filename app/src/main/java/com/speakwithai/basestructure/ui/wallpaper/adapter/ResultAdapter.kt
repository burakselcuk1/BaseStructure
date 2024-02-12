package com.speakwithai.basestructure.ui.wallpaper.adapter

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.data.model.response.wallpaper.Photo


class ResultAdapter(private var mList: ArrayList<Photo>) : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    private var clickListener: ResultClickListener? = null

    fun setClickListener(listener: ResultClickListener) {
        clickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.result_adapter_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]


        Glide
            .with(holder.itemView.context)
            .load(ItemsViewModel.src.portrait)
            .centerCrop()
            .placeholder(R.drawable.logg)

            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    p0: GlideException?,
                    p1: Any?,
                    p2: Target<Drawable>?,
                    p3: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    p0: Drawable?,
                    p1: Any?,
                    p2: Target<Drawable>?,
                    p3: DataSource?,
                    p4: Boolean
                ): Boolean {


                    return false
                }
            }).into(holder.imageView)


        holder.imageView.setOnClickListener {
            clickListener?.editorResultItemClick(mList[position])
         }
    }

    fun setresult(s: ArrayList<Photo>) {
        this.mList = s
    }

    fun addresult(s: ArrayList<Photo>) {
        this.mList.addAll(s)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.resultimage)
    }
}