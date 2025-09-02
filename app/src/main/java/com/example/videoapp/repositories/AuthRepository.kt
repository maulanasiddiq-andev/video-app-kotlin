package com.example.videoapp.repositories

import com.example.videoapp.models.BaseResponse
import com.example.videoapp.models.TokenModel
import com.example.videoapp.requests.LoginRequest
import com.example.videoapp.requests.OtpRequest
import com.example.videoapp.requests.RegisterRequest
import com.example.videoapp.services.AuthService

class AuthRepository(private val service: AuthService) {
    suspend fun login(email: String, password: String): BaseResponse<TokenModel> {
        val result = service.login(LoginRequest(email, password))

        if (!result.succeed) throw Exception(result.messages[0])

        return result
    }

    suspend fun register(email: String, username: String, name: String, password: String): BaseResponse<TokenModel> {
        val result = service.register(RegisterRequest(email, username, name, password))

        if (!result.succeed) throw Exception(result.messages[0])

        return result
    }

    suspend fun checkOtpValidation(email: String, otp: String): BaseResponse<TokenModel> {
        val result = service.checkOtpValidation(OtpRequest(email, otp))

        if (!result.succeed) throw Exception(result.messages[0])

        return result
    }
}