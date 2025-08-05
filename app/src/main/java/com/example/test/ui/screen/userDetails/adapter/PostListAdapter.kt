package com.example.test.ui.screen.userDetails.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.data.model.Post
import com.example.test.databinding.ListItemPostBinding
import com.example.test.ui.screen.userDetails.viewHolder.PostViewHolder

class PostListAdapter() : RecyclerView.Adapter<PostViewHolder>() {

    private var postList: List<Post> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ListItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = postList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newPostList: List<Post>) {
        postList = newPostList
        notifyDataSetChanged()
    }
}