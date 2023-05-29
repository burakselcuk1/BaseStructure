package com.example.basestructure.ui.explore.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.basestructure.R
import com.example.basestructure.model.local.MessageEntity

class DatabaseAdapter(private val messageList: MutableList<MessageEntity>,
                      private val onItemClicked: (date: String) -> Unit // Burada callback'i ekliyoruz
) :
    RecyclerView.Adapter<DatabaseAdapter.DatabaseViewHolder>() {

    inner class DatabaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userHistory: TextView = itemView.findViewById(R.id.user_history)
        val userHistory2: TextView = itemView.findViewById(R.id.chat_history)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatabaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.database_item, parent, false)
        return DatabaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: DatabaseViewHolder, position: Int) {
        val currentItem = messageList[position]
        holder.userHistory.text = currentItem.content
        holder.userHistory2.text = currentItem.dateTime

        holder.itemView.setOnClickListener {
            val date = currentItem.dateTime.substring(0, 10)
            onItemClicked(date) // Tıklama işlemi gerçekleştiğinde callback'i çağırıyoruz
        }

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
