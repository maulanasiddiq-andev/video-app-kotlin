package com.example.videoapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

@Composable
fun AuthInputComponent(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier,
    isPassword: Boolean = false
) {
    if (isPassword) {
        var isPasswordVisible by remember { mutableStateOf(false) }

        TextField(
            modifier = modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                placeholder,
                    style = TextStyle(
                        fontSize = 16.sp
                    )
                )
            },
            shape = RectangleShape,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp
            ),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = "Toggle Password Visibility"
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    } else {
        TextField(
            modifier = modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
                    style = TextStyle(
                        fontSize = 16.sp
                    )
                )
            },
            shape = RectangleShape,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}
