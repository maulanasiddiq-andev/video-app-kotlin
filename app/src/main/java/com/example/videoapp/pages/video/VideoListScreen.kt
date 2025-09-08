package com.example.videoapp.pages.video

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.videoapp.BuildConfig
import com.example.videoapp.components.TopBarComponent
import com.example.videoapp.components.TopBarMenu
import com.example.videoapp.local.TokenManager
import com.example.videoapp.repositories.VideoRepository
import com.example.videoapp.viewModels.video.VideoListViewModel
import com.example.videoapp.viewModels.video.VideoListViewModelFactory
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun VideoListScreen(navHostController: NavHostController, tokenManager: TokenManager, repository: VideoRepository) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val viewModel: VideoListViewModel = viewModel(factory = VideoListViewModelFactory(repository))

    val isLoading = viewModel.isLoading.value
    val videos = viewModel.videos.value
    val message = viewModel.message.value

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    LaunchedEffect(Unit) {
        if (videos.isEmpty()) {
            viewModel.getVideos()
        }
    }

    Scaffold(
        topBar = {
            TopBarComponent(
                "List Video",
                actions = listOf(
                    TopBarMenu(
                        Icons.Default.Logout,
                        onClick = {
                            scope.launch {
                                tokenManager.clearToken()
                            }

                            navHostController.navigate("login") {
                                popUpTo("videoList") { inclusive = true }
                            }
                        }
                    )
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .clickable{
                        navHostController.navigate("videoCreate")
                    }
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.padding(0.dp, 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add"
                    )
                    Text("Buat Video")
                }
            }
            if (isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color(0xFF2196f3))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(videos) { video ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f),
                                model = "${BuildConfig.FILE_URL}${video.thumbnailUrl}",
                                contentDescription = "Picture"
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp, 0.dp)
                            ) {
                                Text(
                                    video.title,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}