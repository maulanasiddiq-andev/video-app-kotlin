package com.example.videoapp.viewModels.video

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.videoapp.models.VideoModel
import com.example.videoapp.repositories.VideoRepository
import kotlinx.coroutines.launch

class VideoListViewModel(private val repository: VideoRepository): ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _videos = mutableStateOf<List<VideoModel>>(emptyList())
    val videos: State<List<VideoModel>> = _videos

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> = _message

    fun getVideos() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = repository.getVideos()

                result.data?.let {
                    it.items.forEach { item ->
                        _videos.value = _videos.value + item
                    }
                }

                Log.d("Video List", result.toString())
            } catch (e: Exception) {
                _message.value
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}

class VideoListViewModelFactory(private val repository: VideoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}