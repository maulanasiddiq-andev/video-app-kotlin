package com.example.videoapp.viewModels.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.videoapp.local.TokenManager
import com.example.videoapp.repositories.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val tokenManager: TokenManager,
    private val navController: NavController
): ViewModel() {
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
                result.data?.token?.let { token -> tokenManager.storeToken(token) }

                _message.value = result.messages[0]
                navController.navigate("videoList") {
                    popUpTo("login") { inclusive = true }
                }
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

class LoginViewModelFactory(private val tokenManager: TokenManager, private val navController: NavController) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(tokenManager, navController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}