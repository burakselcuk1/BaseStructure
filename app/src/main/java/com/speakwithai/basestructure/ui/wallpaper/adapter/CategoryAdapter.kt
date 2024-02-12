package com.speakwithai.basestructure.ui.wallpaper.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.corylustech.superai.presentation.wallpaper.model.CategoryClass
import com.speakwithai.basestructure.R

class CategoryAdapter(private val mList: ArrayList<ArrayList<CategoryClass>>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var clickListener: CategoryClickListener? = null
    fun setClickListener(listener: CategoryClickListener) {
        clickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val txt=mList[position][0].categoryname
        val txt2=mList[position][1].categoryname
        val url1=mList[position][0].categoryurl
        val url2=mList[position][1].categoryurl
        Glide
            .with(holder.itemView.context)
            .load(url1)
            .centerCrop()
            .placeholder(R.drawable.logg)
            .into(holder.cat1);
        Glide
            .with(holder.itemView.context)
            .load(url2)
            .centerCrop()
            .placeholder(R.drawable.logg)
            .into(holder.cat2);
        holder.cat1namw.text=txt
        holder.cat2namw.text=txt2
        holder.rlt1.setOnClickListener {
            clickListener?.categoryItemClick(txt)
        }
        holder.rlt2.setOnClickListener {
            clickListener?.categoryItemClick(txt2)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cat1: ImageView = itemView.findViewById(R.id.cat1)
        val cat2: ImageView = itemView.findViewById(R.id.cat2)
        val cat1namw: TextView =itemView.findViewById(R.id.cat1name)
        val cat2namw: TextView =itemView.findViewById(R.id.cat2name)
        val rlt1: RelativeLayout =itemView.findViewById(R.id.rlt1)
        val rlt2: RelativeLayout =itemView.findViewById(R.id.rlt2)


    }
}