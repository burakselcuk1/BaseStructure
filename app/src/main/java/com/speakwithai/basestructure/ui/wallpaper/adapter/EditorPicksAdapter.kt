package com.speakwithai.basestructure.ui.wallpaper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.data.model.response.wallpaper.Photo

class EditorPicksAdapter(private val editor: ArrayList<Photo>) : RecyclerView.Adapter<EditorPicksAdapter.CustomViewHolder>() {
    private var clickListener: EditorPicksClickListener? = null

    fun setClickListener(listener: EditorPicksClickListener) {
        clickListener = listener
    }
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var editorImage = itemView.findViewById<ImageView>(R.id.editorimage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.editor_item, parent, false)
        return CustomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val s = editor[position].src.medium
        val s2 = editor[position].src.original
        holder.itemView.setOnClickListener {
            clickListener?.editorPicksItemClick(editor[position])
        }

        Glide.with(holder.itemView.context)
            .load(s)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.logg)
                .fitCenter()
                .centerCrop())
            .into(holder.editorImage)
    }

    override fun getItemCount(): Int {
        return editor.size
    }
}
