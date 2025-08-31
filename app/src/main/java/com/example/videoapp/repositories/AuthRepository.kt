package com.example.videoapp.repositories

import android.util.Log
import com.example.videoapp.models.BaseResponse
import com.example.videoapp.models.TokenModel
import com.example.videoapp.requests.LoginRequest
import com.example.videoapp.services.RetrofitInstance

class AuthRepository {
    private val service = RetrofitInstance.authService

    suspend fun login(email: String, password: String): BaseResponse<TokenModel> {
        Log.d("Auth Repository", "Running Login")
        val result = service.login(LoginRequest(email, password))
        Log.d("Auth Repository", result.toString())

        if (!result.succeed) throw Exception(result.messages[0])

        return result
    }
}