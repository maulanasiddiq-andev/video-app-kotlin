package com.example.videoapp.viewModels.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videoapp.repositories.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val repository = AuthRepository()

    private val _isLoading = mutableStateOf<Boolean>(false)
    val isLoading: State<Boolean> = _isLoading

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> = _message

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = repository.login(email, password)
                _message.value = result.messages[0]
            } catch (e: Exception) {
                _message.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}