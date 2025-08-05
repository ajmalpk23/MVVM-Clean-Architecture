package com.example.test.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.data.model.User
import com.example.test.data.network.ApiResult
import com.example.test.data.network.NetworkCallback
import com.example.test.repository.UserRepository
import com.example.test.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    private val _userLiveData = MutableLiveData<ApiResult<MutableList<User>>>()
    val userLiveData: LiveData<ApiResult<MutableList<User>>> get() = _userLiveData

    fun loadUser() {
        _userLiveData.postValue(ApiResult.Loading)
        userRepository.getUserList(object : NetworkCallback{
            override fun onSuccess(result: Any?) {
                (result as? MutableList<User>)?.let { userList ->
                    _userLiveData.postValue(ApiResult.Success(userList))
                }
            }

            override fun onError(errorMessage: String) {

            }

        })
    }
}
