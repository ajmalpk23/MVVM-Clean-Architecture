package com.example.test.ui.screen.userDetails.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.test.R
import com.example.test.data.model.User
import com.example.test.databinding.ActivityUserDetailsBinding
import com.example.test.ui.base.BaseActivity
import com.example.test.viewmodel.UserDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.data.network.ApiResult
import com.example.test.ui.screen.userDetails.adapter.PostListAdapter

@AndroidEntryPoint
class ActivityUserDetails : BaseActivity() {
   private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var postListAdapter: PostListAdapter
    private val userDetailsViewModel: UserDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        postListAdapter = PostListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = postListAdapter

        binding.toolbar.setNavigationIcon(R.drawable.ic_back_white)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            userDetailsViewModel.selectedUser = intent.getParcelableExtra("user", User::class.java)
        } else {
            @Suppress("DEPRECATION")
            userDetailsViewModel.selectedUser = intent.getParcelableExtra("user")
        }

        updateUI()
        updatePostListUI()
    }

    private fun updatePostListUI() {
        userDetailsViewModel.postListLiveData.observe(this, Observer { result ->
            when (result) {
                is ApiResult.Loading -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.layoutPostListShimmer.root.visibility = View.VISIBLE
                    binding.layoutPostListShimmer.shimmerLayout.startShimmer()
                }

                is ApiResult.Success -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.layoutPostListShimmer.shimmerLayout.stopShimmer()
                    binding.layoutPostListShimmer.root.visibility = View.GONE
                    postListAdapter.updateList(result.data)
                }

                is ApiResult.Error -> {
                    binding.layoutPostListShimmer.shimmerLayout.stopShimmer()
                    binding.layoutPostListShimmer.root.visibility = View.GONE
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        userDetailsViewModel.loadPostList()
    }

    private fun updateUI() {
        binding.layoutUser.txtFullName.text = userDetailsViewModel.selectedUser?.name
        binding.layoutUser.username.text = userDetailsViewModel.selectedUser?.username
        binding.layoutUser.email.text = userDetailsViewModel.selectedUser?.email
        binding.layoutUser.website.text = userDetailsViewModel.selectedUser?.website
        binding.layoutUser.phone.text = userDetailsViewModel.selectedUser?.phone

        binding.layoutUser.email.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:${userDetailsViewModel.selectedUser?.email}".toUri()
            }
            startActivity(intent)
        }

        binding.layoutUser.phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = "tel:${userDetailsViewModel.selectedUser?.phone}".toUri()
            }
            startActivity(intent)
        }

        binding.layoutUser.website.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = userDetailsViewModel.selectedUser?.website?.toUri()
            }
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}