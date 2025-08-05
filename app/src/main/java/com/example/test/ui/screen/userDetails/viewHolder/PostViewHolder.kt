package com.example.test.ui.screen.userDetails.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.test.data.model.Post
import com.example.test.data.model.User
import com.example.test.databinding.ListItemPostBinding
import com.example.test.databinding.ListItemUserBinding
import com.example.test.ui.screen.userList.`interface`.UserListAdapterInterface

class PostViewHolder(private val binding: ListItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.txtTitle.text = post.title
        binding.txtBody.text = post.body
    }
}