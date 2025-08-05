package com.example.test.data.network

enum class ApiEndPoint (val value: String) {
    USERS("users"),
    POSTS("posts?userId={userId}")
}