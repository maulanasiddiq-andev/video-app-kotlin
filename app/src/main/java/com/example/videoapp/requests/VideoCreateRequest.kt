package com.example.videoapp.requests

data class VideoCreateRequest(
    val title: String,
    val description: String,
    val isCommentActive: Boolean,
    val isLikeVisible: Boolean,
    val isDislikeVisible: Boolean
)