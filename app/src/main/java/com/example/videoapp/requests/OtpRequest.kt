package com.example.videoapp.requests

data class OtpRequest(
    val email: String,
    val otpCode: String
)