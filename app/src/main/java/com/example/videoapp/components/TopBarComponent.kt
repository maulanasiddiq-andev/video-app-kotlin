package com.example.videoapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import android.graphics.Color as AndroidColor

@ExperimentalMaterial3Api
@Composable
fun TopBarComponent(
    title: String,
    onNavigationClick: (() -> Unit)? = null,
    actions: List<TopBarMenu> = emptyList()
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFF2196f3),
            titleContentColor = Color(AndroidColor.WHITE),
            actionIconContentColor = Color(0xFFFFFFFF),
            navigationIconContentColor = Color(0xFFFFFFFF)
        ),
        navigationIcon = {
            if (onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        },
        actions = {
            actions.forEach { menu ->
                IconButton(
                    onClick = { menu.onClick() }
                ) {
                    Icon(
                        imageVector = menu.icon,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

data class TopBarMenu(
    val icon: ImageVector,
    val onClick: () -> Unit
)