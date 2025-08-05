package com.example.test.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONException
import org.json.JSONObject

@SuppressLint("ParcelCreator")
@Parcelize
data class User(
    var id: Long? = null,
    var name: String? = null,
    var username: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var website: String? = null
) : Parcelable {

    @Throws(JSONException::class)
    constructor(json: JSONObject) : this() {
        parseUser(json)
    }

    @Throws(JSONException::class)
    private fun parseUser(json: JSONObject) {
        id = json.optLong("id", id ?: 0)
        name = json.optString("name", name ?: "")
        username = json.optString("username", username ?: "")
        email = json.optString("email", email ?: "")
        phone = json.optString("phone", phone ?: "")
        website = json.optString("website", website ?: "")
    }
}
