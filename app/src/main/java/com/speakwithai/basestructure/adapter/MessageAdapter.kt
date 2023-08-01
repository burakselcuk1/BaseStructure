package com.speakwithai.basestructure.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.model.Message


class MessageAdapter(private var messageList: MutableList<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private var showCopyIcon = false

    companion object {
        const val SENT_BY_ME = 0
        const val SENT_BY_BOT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            SENT_BY_ME -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.user_chat, parent, false)
                MessageViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.bot_chat, parent, false)
                MessageViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        holder.bind(message)

        if (showCopyIcon && message.sentBy == Message.SENT_BY_BOT) {
            holder.copyIcon?.visibility = View.VISIBLE
        } else {
            holder.copyIcon?.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return when (message.sentBy) {
            Message.SENT_BY_ME -> SENT_BY_ME
            else -> SENT_BY_BOT
        }
    }
    fun updateData(newMessageList: List<Message>) {
        this.messageList.clear()
        this.messageList.addAll(newMessageList)
        notifyDataSetChanged()
    }

    fun addMessage(message: Message) {
        this.messageList.add(message)
        notifyItemInserted(messageList.size - 1)
    }

    fun removeTypingIndicator() {
        if (messageList.lastOrNull()?.sentBy == Message.SENT_BY_BOT) {
            messageList.removeAt(messageList.size - 1)
            notifyItemRemoved(messageList.size)
        }
    }

    fun updateLastMessage(newMessage: Message) {
        if (messageList.isNotEmpty() && messageList.last().sentBy == Message.SENT_BY_BOT) {
            messageList[messageList.size - 1] = newMessage
            notifyItemChanged(messageList.size - 1)
        } else {
            addMessage(newMessage)
        }
    }

    fun setMessages(newMessageList: List<Message>) {
        this.messageList.clear()
        this.messageList.addAll(newMessageList)
        notifyDataSetChanged()
    }




    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val leftChatTextView: TextView? = itemView.findViewById(R.id.left_chat_text_view)
        private val leftChatTimestamp: TextView? = itemView.findViewById(R.id.left_chat_timestamp)
        private val rightChatTextView: TextView? = itemView.findViewById(R.id.right_chat_text_view)
        private val rightChatTimestamp: TextView? = itemView.findViewById(R.id.right_chat_timestamp)
        val copyIcon: ImageView? = itemView.findViewById(R.id.copy_icon)

        init {
            leftChatTextView?.movementMethod = LinkMovementMethod.getInstance()
        }

        fun bind(message: Message) {
            when (message.sentBy) {
                Message.SENT_BY_ME -> {
                    rightChatTextView?.text = message.message
                    rightChatTimestamp?.text = message.timestamp
                }
                else -> {
                    // HTML olarak işle
                    val spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(message.message, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        @Suppress("DEPRECATION")
                        Html.fromHtml(message.message)
                    }
                    leftChatTextView?.text = spanned
                    leftChatTimestamp?.text = message.timestamp
                    copyIcon?.setOnClickListener {
                        // Burada mesajı kopyalarız.
                        val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("bot_message", message.message)
                        clipboard.setPrimaryClip(clip)

                        // Kopyalamanın başarılı olduğunu bildir
                        Toast.makeText(itemView.context, "Message copied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun setCopyIconVisibility(visible: Boolean) {
        showCopyIcon = visible
        notifyDataSetChanged()
    }
}
