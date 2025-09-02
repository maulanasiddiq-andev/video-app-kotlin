package com.example.videoapp.viewModels.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.videoapp.local.TokenManager
import com.example.videoapp.repositories.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val tokenManager: TokenManager,
    private val repository: AuthRepository
): ViewModel() {
    private val _isLoading = mutableStateOf<Boolean>(false)
    val isLoading: State<Boolean> = _isLoading

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> = _message

    private val _succeed = mutableStateOf(false)
    val succeed: State<Boolean> = _succeed

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = repository.login(email, password)
                result.data?.token?.let { token -> tokenManager.storeToken(token) }

                _succeed.value = true
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

class LoginViewModelFactory(private val tokenManager: TokenManager, private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(tokenManager, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}