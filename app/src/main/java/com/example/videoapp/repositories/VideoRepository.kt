package com.example.videoapp.repositories

import com.example.videoapp.models.BaseResponse
import com.example.videoapp.models.SearchResponse
import com.example.videoapp.models.VideoModel
import com.example.videoapp.requests.VideoCreateRequest
import com.example.videoapp.services.VideoService
import com.example.videoapp.utils.convertImageToMultipartBody
import com.example.videoapp.utils.convertStringToRequestBody
import com.example.videoapp.utils.convertVideoToMultipartBody
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class VideoRepository(private val service: VideoService) {
    suspend fun getVideos(): BaseResponse<SearchResponse<VideoModel>> {
        val result = service.getVideos()

        if (!result.succeed) throw Exception(result.messages[0])

        return result
    }

    suspend fun getVideoById(id: String): BaseResponse<VideoModel> {
        val result = service.getVideoById(id)

        if (!result.succeed) throw Exception(result.messages[0])

        return result
    }

    suspend fun createVideo(
        title: String,
        description: String,
        isCommentActive: Boolean,
        isLikeVisible: Boolean,
        isDislikeVisible: Boolean,
        thumbnail: File,
        video: File
    ): BaseResponse<VideoModel> {
        val thumbnailPart = convertImageToMultipartBody(thumbnail, "thumbnail")
        val videoPart = convertVideoToMultipartBody(video)

        val result = service.createVideo(
            convertStringToRequestBody(title),
            convertStringToRequestBody(description),
            convertStringToRequestBody(isCommentActive.toString()),
            convertStringToRequestBody(isLikeVisible.toString()),
            convertStringToRequestBody(isDislikeVisible.toString()),
            thumbnailPart,
            videoPart
        )

        if (!result.succeed) throw Exception(result.messages[0])

        return result
    }
}