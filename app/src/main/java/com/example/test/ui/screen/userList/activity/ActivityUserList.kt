package com.example.test.ui.screen.userList.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.R
import com.example.test.data.model.User
import com.example.test.data.network.ApiResult
import com.example.test.databinding.ActivityMainBinding
import com.example.test.ui.base.BaseActivity
import com.example.test.ui.screen.userDetails.activity.ActivityUserDetails
import com.example.test.ui.screen.userList.adapter.UserListAdapter
import com.example.test.ui.screen.userList.`interface`.UserListAdapterInterface
import com.example.test.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityUserList : BaseActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserListAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        updateUserListUI()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = userAdapter
        binding.layoutUserListShimmer.root.visibility = View.GONE

        binding.swipeRefreshLayout.setOnRefreshListener {
            userViewModel.loadUserList()
        }
    }

    private fun updateUserListUI() {
        userAdapter = UserListAdapter(object : UserListAdapterInterface {
            override fun onClickListener(user: User) {
                val intent = Intent(this@ActivityUserList, ActivityUserDetails::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }
        })

        userViewModel.userListLiveData.observe(this, Observer { result ->
            when (result) {
                is ApiResult.Loading -> {
                    if (!binding.swipeRefreshLayout.isRefreshing) {
                        binding.layoutUserListShimmer.root.visibility = View.VISIBLE
                        binding.layoutUserListShimmer.shimmerLayout.startShimmer()
                    }
                }

                is ApiResult.Success -> {
                    binding.layoutUserListShimmer.shimmerLayout.stopShimmer()
                    binding.layoutUserListShimmer.root.visibility = View.GONE
                    userAdapter.updateList(result.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                }

                is ApiResult.Error -> {
                    binding.layoutUserListShimmer.shimmerLayout.stopShimmer()
                    binding.layoutUserListShimmer.root.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        userViewModel.loadUserList()
    }
}