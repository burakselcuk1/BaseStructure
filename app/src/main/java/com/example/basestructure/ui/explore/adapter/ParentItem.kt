package com.example.basestructure.ui.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basestructure.R
import com.example.basestructure.model.Child
import com.example.basestructure.model.Parent


class MyAdapter(private val parents: List<Parent>) : RecyclerView.Adapter<ParentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_parent, parent, false)
        return ParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.bind(parents[position])
    }

    override fun getItemCount() = parents.size
}
