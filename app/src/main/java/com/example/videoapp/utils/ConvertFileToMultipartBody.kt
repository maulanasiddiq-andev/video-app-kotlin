package com.example.videoapp.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun convertVideoToMultipartBody(video: File): MultipartBody.Part {
    val requestFile = video.asRequestBody("video/*".toMediaTypeOrNull())

    return MultipartBody.Part.createFormData(
        "video",
        video.name,
        requestFile
    )
}

fun convertImageToMultipartBody(image: File, name: String): MultipartBody.Part {
    val requestFile = image.asRequestBody("image/*".toMediaTypeOrNull())

    return MultipartBody.Part.createFormData(
        name,
        image.name,
        requestFile
    )
}

fun convertStringToRequestBody(name: String): RequestBody {
    return name.toRequestBody("text/plain".toMediaTypeOrNull())
}