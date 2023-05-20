package com.example.basestructure.ui.explore.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basestructure.R
import com.example.basestructure.model.Child
import com.example.basestructure.model.Parent


class ParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val iconView: ImageView = view.findViewById(R.id.icon)
    private val titleView: TextView = view.findViewById(R.id.title)
    private val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

    fun bind(parent: Parent) {
        iconView.setImageDrawable(parent.icon)
        titleView.text = parent.title
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ChildAdapter(parent.children)
        }
    }
}

class ChildViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textView: TextView = view.findViewById(R.id.textView)

    fun bind(child: Child) {
        textView.text = child.text
    }
}
