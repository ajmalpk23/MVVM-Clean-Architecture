package com.example.test.repository

import com.example.test.data.model.User
import com.example.test.data.network.ApiEndPoint
import com.example.test.data.network.NetworkCallback
import com.example.test.data.network.WebService
import com.example.test.repository.base.BaseRepository
import org.json.JSONArray
import org.json.JSONException
import javax.inject.Inject

class UserRepository @Inject constructor(): BaseRepository() {

    fun getUserList(callback: NetworkCallback) {

        WebService(ApiEndPoint.USERS.value).get(object : NetworkCallback {
            override fun onSuccess(result: Any?) {
                try {
                    (result as? String)?.let {
                        callback.onSuccess(handleUserListResponse(result))
                    }
                } catch (e: JSONException) {

                }
            }

            override fun onError(errorMessage: String) {

            }

            @Throws(JSONException::class)
            private fun handleUserListResponse(result: String?): MutableList<User>? {
                result?.let {
                    JSONArray(result).let { userArray ->
                        val userList = mutableListOf<User>()
                        for (i in 0 until userArray.length()) {
                            userList.add(User(userArray.optJSONObject(i)))
                        }
                        return userList
                    }
                }
                return null
            }

        })
    }
}