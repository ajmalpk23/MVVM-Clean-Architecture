package com.example.test.ui.screen.userList.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.data.model.User
import com.example.test.databinding.ListItemUserBinding
import com.example.test.ui.screen.userList.`interface`.UserListAdapterInterface
import com.example.test.ui.screen.userList.viewHolder.UserViewHolder

class UserListAdapter(val listener: UserListAdapterInterface) : RecyclerView.Adapter<UserViewHolder>() {

    private var userList: List<User> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user, listener)
    }

    override fun getItemCount(): Int = userList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newUserList: List<User>) {
        userList = newUserList
        notifyDataSetChanged()
    }

}