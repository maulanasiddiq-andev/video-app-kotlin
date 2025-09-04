package com.example.videoapp.models

data class UserModel(
    val email: String,
    val name: String,
    val username: String,
    val imageUrl: String
) : BaseModel()