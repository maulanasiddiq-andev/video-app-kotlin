package com.example.videoapp.services

import com.example.videoapp.models.BaseResponse
import com.example.videoapp.models.SearchResponse
import com.example.videoapp.models.VideoModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface VideoService {
    @GET("video")
    suspend fun getVideos(): BaseResponse<SearchResponse<VideoModel>>

    @GET("video/{id}")
    suspend fun getVideoById(@Path("id") id: String): BaseResponse<VideoModel>

    @Multipart
    @POST("video")
    suspend fun createVideo(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("isCommentActive") isCommentActive: RequestBody,
        @Part("isLikeVisible") isLikeVisible: RequestBody,
        @Part("isDislikeVisible") isDislikeVisible: RequestBody,
        @Part thumbnail: MultipartBody.Part,
        @Part video: MultipartBody.Part
    ): BaseResponse<VideoModel>
}