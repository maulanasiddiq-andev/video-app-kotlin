package com.example.videoapp.services

import com.example.videoapp.models.BaseResponse
import com.example.videoapp.models.TokenModel
import com.example.videoapp.requests.LoginRequest
import com.example.videoapp.requests.OtpRequest
import com.example.videoapp.requests.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<TokenModel>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): BaseResponse<TokenModel>

    @POST("auth/otp")
    suspend fun checkOtpValidation(@Body request: OtpRequest): BaseResponse<TokenModel>
}