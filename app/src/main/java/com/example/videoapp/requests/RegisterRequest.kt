package com.example.videoapp.requests

data class RegisterRequest(
    val email: String,
    val username: String,
    val name: String,
    val password: String
)