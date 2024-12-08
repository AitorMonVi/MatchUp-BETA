package com.example.matchup_beta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val userList: MutableList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameAndAge: TextView = view.findViewById(R.id.text_name_age)
        val status: TextView = view.findViewById(R.id.text_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_scroll,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        val nameAndAge = "${user.name}, ${user.age} años"
        holder.nameAndAge.text = nameAndAge

        holder.status.text = user.status
        if(user.status == "Online") {
            holder.status.setBackgroundResource(R.drawable.bg_online)
        } else {
            holder.status.setBackgroundResource(R.drawable.bg_offline)
        }
    }

    override fun getItemCount(): Int = userList.size
    fun updateData(newUserList: List<User>) {
        (userList).clear()
        userList.addAll(newUserList)
        notifyDataSetChanged()
    }
}