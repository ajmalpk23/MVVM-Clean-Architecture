package com.example.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test.data.model.Post
import com.example.test.data.model.User
import com.example.test.data.network.ApiResult
import com.example.test.data.network.NetworkCallback
import com.example.test.repository.PostRepository
import com.example.test.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(private val postRepository: PostRepository) : BaseViewModel() {

    var selectedUser: User? = null

    private val _postListLiveData = MutableLiveData<ApiResult<MutableList<Post>>>()
    val postListLiveData: LiveData<ApiResult<MutableList<Post>>> get() = _postListLiveData

    fun loadPostList() {
        _postListLiveData.postValue(ApiResult.Loading)
        postRepository.getPostList(selectedUser?.id ?: 0,object : NetworkCallback {
            override fun onSuccess(result: Any?) {
                (result as? MutableList<Post>)?.let { postList ->
                    _postListLiveData.postValue(ApiResult.Success(postList))
                }
            }

            override fun onError(errorMessage: String) {
                _postListLiveData.postValue(ApiResult.Error(errorMessage))
            }
        })
    }
}
