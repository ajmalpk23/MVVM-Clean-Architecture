package com.example.test.ui.screen.userList.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.test.data.model.User
import com.example.test.databinding.ListItemUserBinding
import com.example.test.ui.screen.userList.`interface`.UserListAdapterInterface

class UserViewHolder(private val binding: ListItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User, listener: UserListAdapterInterface) {
        binding.userNameTextView.text = user.name
        binding.root.setOnClickListener { listener.onClickListener(user)  }
    }
}