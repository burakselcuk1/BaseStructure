package com.example.basestructure.ui.explore.adapter

import android.content.res.ColorStateList
import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basestructure.R
import com.example.basestructure.model.Child
import com.example.basestructure.model.Parent
import com.google.android.material.card.MaterialCardView


class ParentViewHolder(view: View, private val onChildItemClickListener: (Child) -> Unit) : RecyclerView.ViewHolder(view) {
    private val iconView: ImageView = view.findViewById(R.id.icon)
    private val titleView: TextView = view.findViewById(R.id.title)
    private val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

    fun bind(parent: Parent) {
        iconView.setImageDrawable(parent.icon)
        titleView.text = parent.title
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ChildAdapter(parent.children, onChildItemClickListener)
        }
    }
}


class ChildViewHolder(itemView: View, private val onItemClickListener: (Child) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.textView)
    private val layout: MaterialCardView = itemView.findViewById(R.id.item_child)
    private val colors = listOf(R.color.child_one, R.color.child_two, R.color.child_three, R.color.child_four, R.color.child_five)

    fun bind(child: Child, position: Int) {
        textView.text = child.text

        // Assign a different background color depending on position
        val color = ContextCompat.getColor(layout.context, colors[position % colors.size])
        layout.backgroundTintList = ColorStateList.valueOf(color)

        // Adjusting only the left margin
        val params = layout.layoutParams as RecyclerView.LayoutParams
        params.setMargins(20.dpToPx(), params.topMargin, params.rightMargin, params.bottomMargin)
        layout.layoutParams = params

        // Set the click listener for the layout
        layout.setOnClickListener {
            onItemClickListener(child)
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }
}

