package com.example.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test.data.model.User
import com.example.test.data.network.ApiResult
import com.example.test.data.network.NetworkCallback
import com.example.test.repository.UserRepository
import com.example.test.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    private val _userListLiveData = MutableLiveData<ApiResult<MutableList<User>>>()
    val userListLiveData: LiveData<ApiResult<MutableList<User>>> get() = _userListLiveData

    fun loadUserList() {
        _userListLiveData.postValue(ApiResult.Loading)
        userRepository.getUserList(object : NetworkCallback{
            override fun onSuccess(result: Any?) {
                (result as? MutableList<User>)?.let { userList ->
                    _userListLiveData.postValue(ApiResult.Success(userList))
                }
            }
            override fun onError(errorMessage: String) {
                _userListLiveData.postValue(ApiResult.Error(errorMessage))
            }
        })
    }
}
