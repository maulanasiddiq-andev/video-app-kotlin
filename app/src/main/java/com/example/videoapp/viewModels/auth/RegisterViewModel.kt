package com.example.videoapp.viewModels.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.videoapp.repositories.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository): ViewModel() {
    private val _isLoadingRegister = mutableStateOf<Boolean>(false)
    val isLoadingRegister: State<Boolean> = _isLoadingRegister

    private val _registerSucceed = mutableStateOf(false)
    val registerSucceed: State<Boolean> = _registerSucceed

    private val _isLoadingOtp = mutableStateOf<Boolean>(false)
    val isLoadingOtp: State<Boolean> = _isLoadingOtp

    private val _otpSucceed = mutableStateOf(false)
    val otpSucceed: State<Boolean> = _otpSucceed

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> = _message

    private val storedEmail = mutableStateOf<String?>(null)

    fun register(email: String, username: String, name: String, password: String) {
        viewModelScope.launch {
            _isLoadingRegister.value = true

            try {
                val result = repository.register(email, username, name, password)
                storedEmail.value = email
                _registerSucceed.value = true
                _message.value = result.messages[0]
            } catch (e: Exception) {
                _message.value = e.message
            } finally {
                _isLoadingRegister.value = false
            }
        }
    }

    fun checkOtpValidation(otp: String) {
        viewModelScope.launch {
            _isLoadingOtp.value = true

            try {
                storedEmail.value?.let {
                    val result = repository.checkOtpValidation(it, otp)
                    _otpSucceed.value = true
                    _message.value = result.messages[0]
                } ?: run {
                    throw Exception("Email belum terisi")
                }
            } catch (e: Exception) {
                _message.value = e.message
            } finally {
                _isLoadingOtp.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}

class RegisterViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun rememberRegisterViewModel(navController: NavController, repository: AuthRepository) : RegisterViewModel {
    val parent = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(navController.graph.id)
    }

    return viewModel(
        parent,
        factory = RegisterViewModelFactory(repository)
    )
}