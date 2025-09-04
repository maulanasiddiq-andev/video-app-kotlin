package com.example.videoapp.models

data class VideoModel(
    val title: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val userId: String,
    val user: UserModel,
    val isCommentActive: Boolean,
    val isLikeVisible: Boolean,
    val isDislikeVisible: Boolean
) : BaseModel()