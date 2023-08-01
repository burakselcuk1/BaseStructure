package com.speakwithai.basestructure.ui.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.model.Child
import com.speakwithai.basestructure.model.Parent


class MyAdapter(private val parents: List<Parent>, private val onChildItemClickListener: (Child) -> Unit) : RecyclerView.Adapter<ParentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_parent, parent, false)
        return ParentViewHolder(view, onChildItemClickListener)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.bind(parents[position])
    }

    override fun getItemCount() = parents.size
}
