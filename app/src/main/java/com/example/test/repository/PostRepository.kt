package com.example.test.repository

import com.example.test.data.model.Post
import com.example.test.data.model.User
import com.example.test.data.network.ApiEndPoint
import com.example.test.data.network.NetworkCallback
import com.example.test.data.network.WebService
import com.example.test.repository.base.BaseRepository
import org.json.JSONArray
import org.json.JSONException
import javax.inject.Inject

class PostRepository @Inject constructor() : BaseRepository() {

    fun getPostList(userID: Long, callback: NetworkCallback) {
        WebService(ApiEndPoint.POSTS.value.replace("userId", "$userID")).get(object :
            NetworkCallback {
            override fun onSuccess(result: Any?) {
                try {
                    (result as? String)?.let {
                        callback.onSuccess(handlePostListResponse(result))
                    }
                } catch (e: JSONException) {
                    callback.onError("Something went wrong")
                }
            }

            override fun onError(errorMessage: String) {
                callback.onError(errorMessage)
            }

            @Throws(JSONException::class)
            private fun handlePostListResponse(result: String?): MutableList<Post>? {
                result?.let {
                    JSONArray(result).let { postArray ->
                        val postList = mutableListOf<Post>()
                        for (i in 0 until postArray.length()) {
                            postList.add(Post(postArray.optJSONObject(i)))
                        }
                        return postList
                    }
                }
                return null
            }
        })
    }
}