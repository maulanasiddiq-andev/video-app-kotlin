package com.example.videoapp.viewModels.video

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.videoapp.repositories.VideoRepository
import com.example.videoapp.requests.VideoCreateRequest
import kotlinx.coroutines.launch
import java.io.File

class VideoCreateViewModel(val repository: VideoRepository): ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _succeed = mutableStateOf(false)
    val succeed: State<Boolean> = _succeed

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> = _message

    fun createVideo(
        title: String,
        description: String,
        isCommentActive: Boolean,
        isLikeVisible: Boolean,
        isDislikeVisible: Boolean,
        thumbnail: File,
        video: File
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = repository.createVideo(
                    title,
                    description,
                    isCommentActive,
                    isLikeVisible,
                    isDislikeVisible,
                    thumbnail,
                    video
                )

                _message.value = result.messages[0]
                _succeed.value = true
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

class VideoCreateViewModelFactory(private val repository: VideoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoCreateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoCreateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}