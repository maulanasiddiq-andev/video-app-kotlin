package com.example.videoapp.models

data class BaseResponse<T>(
    val succeed: Boolean,
    val messages: List<String>,
    val data: T?
)