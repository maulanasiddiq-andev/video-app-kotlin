package com.example.videoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import com.example.videoapp.local.TokenManager
import com.example.videoapp.local.dataStore
import com.example.videoapp.pages.auth.LoginScreen
import com.example.videoapp.pages.auth.OtpVerificationScreen
import com.example.videoapp.pages.auth.RegisterScreen
import com.example.videoapp.pages.video.VideoCreateScreen
import com.example.videoapp.pages.video.VideoListScreen
import com.example.videoapp.repositories.AuthRepository
import com.example.videoapp.repositories.VideoRepository
import com.example.videoapp.services.AuthService
import com.example.videoapp.services.RetrofitInstance
import com.example.videoapp.services.VideoService
import com.example.videoapp.viewModels.auth.CheckTokenViewModel
import com.example.videoapp.viewModels.auth.CheckTokenViewModelFactory
import com.example.videoapp.viewModels.auth.LoginViewModel
import kotlin.getValue
import android.graphics.Color as AndroidColor

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    private val tokenManager by lazy { TokenManager(applicationContext.dataStore) }
    private val retrofit by lazy { RetrofitInstance.create(tokenManager) }

    private val authService by lazy { retrofit.create(AuthService::class.java) }
    private val authRepository by lazy { AuthRepository(authService) }

    private val videoService by lazy { retrofit.create(VideoService::class.java) }
    private val videoRepository by lazy { VideoRepository(videoService) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                AndroidColor.BLACK
            )
        )

        setContent {
            val navController = rememberNavController()
            val checkTokenViewModel: CheckTokenViewModel = viewModel(factory = CheckTokenViewModelFactory(tokenManager))
            val startDestination by checkTokenViewModel.startDestination.collectAsState()

            if (startDestination != null) {
                NavHost(navController = navController, startDestination = startDestination!!) {
                    animatedComposable("login", navController) { LoginScreen(navController, tokenManager = tokenManager, repository = authRepository) }
                    animatedComposable("register", navController) { RegisterScreen(navController, repository = authRepository) }
                    animatedComposable("otpVerification", navController) { OtpVerificationScreen(navController, repository = authRepository) }
                    animatedComposable("videoList", navController) { VideoListScreen(navController, tokenManager = tokenManager, repository = videoRepository) }
                    animatedComposable("videoCreate", navController) { VideoCreateScreen(navController, repository = videoRepository) }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize().background(Color(0xFF2196f3))) {
                    CircularProgressIndicator(
                        color = Color(AndroidColor.WHITE)
                    )
                }
            }
        }
    }
}

fun NavGraphBuilder.animatedComposable(
    route: String,
    navController: NavController,
    content: @Composable (NavController) -> Unit
) {
    composable(
        route,
        enterTransition = {
            // navigating to another screen
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(300)
            )
        },
        exitTransition = {
            // the screen left when navigating to another screen
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(300)
            )
        },
        popEnterTransition = {
            // navigating back to the previous screen
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(300)
            )
        },
        popExitTransition = {
            // the screen left when navigating back
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(300)
            )
        }
    ) {
        content(navController)
    }
}