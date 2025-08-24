package com.example.videoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import com.example.videoapp.pages.auth.LoginScreen
import com.example.videoapp.pages.auth.RegisterScreen
import android.graphics.Color as AndroidColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                AndroidColor.BLACK
            )
        )
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "login") {
                animatedComposable("login", navController) { LoginScreen(navController) }
                animatedComposable("register", navController) { RegisterScreen(navController) }
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