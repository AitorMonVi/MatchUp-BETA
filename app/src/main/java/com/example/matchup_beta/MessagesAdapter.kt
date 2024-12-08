package com.example.matchup_beta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessagesAdapter(
    private val messageUserList: MutableList<MessageUser>,
    private val onItemClick: (MessageUser) -> Unit
) : RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {

    inner class MessagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameAndAge: TextView = view.findViewById(R.id.text_name_age)
        val lastMessage: TextView = view.findViewById(R.id.text_last_message)
        val unreadMessagesCount: TextView = view.findViewById(R.id.text_unread_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val user = messageUserList[position]
        holder.nameAndAge.text = "${user.name}, ${user.age} años"
        holder.lastMessage.text = user.lastMessage
        if (user.unreadMessagesCount > 0) {
            holder.unreadMessagesCount.visibility = View.VISIBLE
            holder.unreadMessagesCount.text = user.unreadMessagesCount.toString()
        } else {
            holder.unreadMessagesCount.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }
    override fun getItemCount(): Int = messageUserList.size
    fun updateData(newMessageList: List<MessageUser>) {
        messageUserList.clear()
        messageUserList.addAll(newMessageList)
        notifyDataSetChanged()
    }
}
