package com.example.videoapp.services

import com.example.videoapp.models.BaseResponse
import com.example.videoapp.models.SearchResponse
import com.example.videoapp.models.VideoModel
import retrofit2.http.GET
import retrofit2.http.Path

interface VideoService {
    @GET("video")
    suspend fun getVideos(): BaseResponse<SearchResponse<VideoModel>>

    @GET("video/{id}")
    suspend fun getVideoById(@Path("id") id: String): BaseResponse<VideoModel>
}