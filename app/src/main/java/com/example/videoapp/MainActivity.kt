package com.example.videoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import com.example.videoapp.local.TokenManager
import com.example.videoapp.local.dataStore
import com.example.videoapp.pages.auth.LoginScreen
import com.example.videoapp.pages.auth.RegisterScreen
import com.example.videoapp.pages.video.VideoListScreen
import com.example.videoapp.viewModels.auth.CheckTokenViewModel
import com.example.videoapp.viewModels.auth.CheckTokenViewModelFactory
import android.graphics.Color as AndroidColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                AndroidColor.BLACK
            )
        )

        val tokenManager = TokenManager(applicationContext.dataStore)
        setContent {
            val navController = rememberNavController()
            val checkTokenViewModel: CheckTokenViewModel = viewModel(factory = CheckTokenViewModelFactory(tokenManager))
            val startDestination by checkTokenViewModel.startDestination.collectAsState()

            if (startDestination != null) {
                NavHost(navController = navController, startDestination = startDestination!!) {
                    animatedComposable("login", navController) { LoginScreen(navController, tokenManager = tokenManager) }
                    animatedComposable("register", navController) { RegisterScreen(navController) }
                    animatedComposable("videoList", navController) { VideoListScreen(navController) }
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