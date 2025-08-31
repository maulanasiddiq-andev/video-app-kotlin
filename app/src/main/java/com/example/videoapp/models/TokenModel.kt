package com.example.videoapp.models

import java.util.Date

data class TokenModel(
    val token: String,
    val refreshToken: String,
    val refreshTokenExpiredTime: Date,
    val isValidLogin: Boolean
)