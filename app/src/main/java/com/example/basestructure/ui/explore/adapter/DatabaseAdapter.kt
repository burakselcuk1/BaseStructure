package com.example.basestructure.ui.explore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.basestructure.R
import com.example.basestructure.model.local.MessageEntity

class DatabaseAdapter(private val messageList: MutableList<MessageEntity>) :
    RecyclerView.Adapter<DatabaseAdapter.DatabaseViewHolder>() {

    inner class DatabaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userHistory: TextView = itemView.findViewById(R.id.user_history)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatabaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.database_item, parent, false)
        return DatabaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: DatabaseViewHolder, position: Int) {
        val currentItem = messageList[position]
        holder.userHistory.text = currentItem.content

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun updateData(newMessages: List<MessageEntity>) {
        messageList.clear()
        messageList.addAll(newMessages)
        notifyDataSetChanged()
    }
}
