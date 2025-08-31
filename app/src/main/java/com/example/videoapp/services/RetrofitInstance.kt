package com.example.videoapp.services

import com.example.videoapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {
    private const val BASE_URL = BuildConfig.BASE_URL

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }
}