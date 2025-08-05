package com.example.test.data.model

import org.json.JSONException
import org.json.JSONObject

data class Post(
    var id: Long? = null,
    var title: String? = null,
    var body: String? = null,
) {

    @Throws(JSONException::class)
    constructor(json: JSONObject) : this() {
        parsePost(json)
    }

    @Throws(JSONException::class)
    private fun parsePost(json: JSONObject) {
        id = json.optLong("id", id ?: 0)
        title = json.optString("title", title ?: "")
        body = json.optString("username", body ?: "")
    }
}
