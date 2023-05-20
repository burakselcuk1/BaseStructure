package com.example.basestructure.ui.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basestructure.R
import com.example.basestructure.model.Child

class ChildAdapter(private val children: List<Child>) : RecyclerView.Adapter<ChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_child, parent, false)
        return ChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bind(children[position])
    }

    override fun getItemCount() = children.size
}
