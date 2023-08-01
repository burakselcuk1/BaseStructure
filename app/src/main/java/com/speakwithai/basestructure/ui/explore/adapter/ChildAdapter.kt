package com.speakwithai.basestructure.ui.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.model.Child

class ChildAdapter(private val children: List<Child>, private val onItemClickListener: (Child) -> Unit) : RecyclerView.Adapter<ChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_child, parent, false)
        return ChildViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bind(children[position], position)
    }

    override fun getItemCount() = children.size
}

