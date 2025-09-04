package com.example.videoapp.repositories

import com.example.videoapp.models.BaseResponse
import com.example.videoapp.models.SearchResponse
import com.example.videoapp.models.VideoModel
import com.example.videoapp.services.VideoService

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
}