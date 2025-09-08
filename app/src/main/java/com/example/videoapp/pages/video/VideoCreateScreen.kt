package com.example.videoapp.pages.video

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.videoapp.components.SwitchComponent
import com.example.videoapp.repositories.VideoRepository
import com.example.videoapp.requests.VideoCreateRequest
import com.example.videoapp.utils.convertUriToFile
import com.example.videoapp.viewModels.video.VideoCreateViewModel
import com.example.videoapp.viewModels.video.VideoCreateViewModelFactory
import java.io.File
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@ExperimentalLayoutApi
//@ExperimentalFoundationApi
@Composable
fun VideoCreateScreen(navHostController: NavHostController, repository: VideoRepository) {
    val context = LocalContext.current
    var selectedImageUrl by remember { mutableStateOf<Uri?>(null) }
    var selectedVideoUrl by remember { mutableStateOf<Uri?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isCommentActive by remember { mutableStateOf(true) }
    var isLikeVisible by remember { mutableStateOf(true) }
    var isDislikeVisible by remember { mutableStateOf(true) }

    val viewModel: VideoCreateViewModel = viewModel(factory = VideoCreateViewModelFactory(repository))
    val message = viewModel.message.value

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    fun submit() {
        if (selectedImageUrl == null || selectedVideoUrl == null) return

        val thumbnail = convertUriToFile(context, selectedImageUrl!!, ".jpg")
        val video = convertUriToFile(context, selectedVideoUrl!!, ".mp4")

        viewModel.createVideo(
            title,
            description,
            isCommentActive,
            isLikeVisible,
            isDislikeVisible,
            thumbnail,
            video
        )
    }

    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val croppedUrl = UCrop.getOutput(it)
                selectedImageUrl = croppedUrl
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            cropError?.printStackTrace()
        }
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val destinationUri = Uri.fromFile(File.createTempFile("cropped_", ".jpg"))
            val options = UCrop.Options().apply {
                setCompressionQuality(90)
            }

            val cropIntent = UCrop.of(it, destinationUri)
                .withAspectRatio(16f, 9f)
                .withOptions(options)
                .getIntent(context)

            cropLauncher.launch(cropIntent)
        }
    }

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedVideoUrl = it
        }
    }

    val exoPlayer = remember(selectedVideoUrl) {
        selectedVideoUrl?.let {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(it))
                prepare()
                playWhenReady = false
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(10.dp,0.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "Thumbnail",
                            style = TextStyle(
                                fontSize = 20.sp
                            )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF2196f3)),
                            contentAlignment = Alignment.Center
                        ) {
                            selectedImageUrl?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentDescription = "thumbnail",
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            } ?: Text("Belum ada thumbnail")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "icon",
                                modifier = Modifier.clickable{imageLauncher.launch("image/*")}
                            )
                        }
                    }
                }
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "Video",
                            style = TextStyle(
                                fontSize = 20.sp
                            )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF2196f3)),
                            contentAlignment = Alignment.Center
                        ) {
                            selectedVideoUrl?.let {
                                AndroidView(
                                    factory = {
                                        PlayerView(it).apply {
                                            player = exoPlayer
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            } ?: Text("Belum ada video")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "icon",
                                modifier = Modifier.clickable{videoLauncher.launch("video/*")}
                            )
                        }
                    }
                }
                item {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = title,
                        onValueChange = {
                            title = it
                        },
                        placeholder = {
                            Text(
                                "Judul",
                                style = TextStyle(
                                    fontSize = 17.sp
                                )
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 17.sp
                        ),
                        singleLine = true,
                        maxLines = 1,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color(0xFF2196f3),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                }
                item {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = description,
                        onValueChange = {
                            description = it
                        },
                        placeholder = {
                            Text(
                                "Deskripsi",
                                style = TextStyle(
                                    fontSize = 17.sp
                                )
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 17.sp
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color(0xFF2196f3),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                }
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        SwitchComponent(
                            value = isCommentActive,
                            onValueChange = { isCommentActive = it }
                        )
                        SwitchComponent(
                            value = isLikeVisible,
                            onValueChange = { isLikeVisible = it }
                        )
                        SwitchComponent(
                            value = isDislikeVisible,
                            onValueChange = { isDislikeVisible = it }
                        )
                    }
                }
                item {
                    Button(
                        onClick = { submit() }
                    ) {
                        Text("Upload")
                    }
                }
            }
        }
    }
}