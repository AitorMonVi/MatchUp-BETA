package com.example.matchup_beta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LikeUserAdapter(
    private val userList: MutableList<User>,
    private val onLikeClick: (User) -> Unit,
    private val onDiscardClick: (User) -> Unit
) : RecyclerView.Adapter<LikeUserAdapter.LikeUserViewHolder>() {

    inner class LikeUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image_profile)
        val nameAndAge: TextView = view.findViewById(R.id.text_name_age)
        val likeButton: ImageView = view.findViewById(R.id.btn_like)
        val discardButton: ImageView = view.findViewById(R.id.btn_discard)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_like, parent, false)
        return LikeUserViewHolder(view)
    }
    override fun onBindViewHolder(holder: LikeUserViewHolder, position: Int) {
        val user = userList[position]
        val nameAndAge = "${user.name}, ${user.age} años"
        holder.nameAndAge.text = nameAndAge
        holder.image.setImageResource(R.drawable.ic_placeholder_profile)
        holder.likeButton.setOnClickListener {
            onLikeClick(user)
        }
        holder.discardButton.setOnClickListener {
            onDiscardClick(user)
        }
    }
    override fun getItemCount(): Int = userList.size

    val currentList: List<User>
        get() = userList


    fun updateData(newUserList: List<User>) {
        userList.clear()
        userList.addAll(newUserList)
        notifyDataSetChanged()
    }

}
