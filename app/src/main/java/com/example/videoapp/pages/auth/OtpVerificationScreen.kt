package com.example.videoapp.pages.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.collections.listOf

@Composable
fun OtpVerificationScreen() {
    val focusManager = LocalFocusManager.current
    var values by remember { mutableStateOf(listOf("", "", "", "")) }
    val focusRequesters = remember {
        listOf(FocusRequester(), FocusRequester(), FocusRequester(), FocusRequester())
    }
    var focusedIndex by remember { mutableStateOf(-1) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF2196f3))
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    "OTP Verification",
                    style = TextStyle(
                        fontSize = 40.sp,
                        color = Color.White
                    )
                )
                Text(
                    "MS Developer",
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(60.dp, 60.dp, 0.dp, 0.dp))
                    .background(Color.White)
                    .padding(20.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    values.forEachIndexed { index, value ->
                        LaunchedEffect(index) {
                            if (index == 0) {
                                focusRequesters[index].requestFocus()
                            }
                        }

                        Box(
                            modifier = Modifier
                                .height(60.dp)
                                .width(50.dp)
                                .border(
                                    border = BorderStroke(
                                        color = if (focusedIndex == index) Color(0xFF2196f3) else Color.Gray,
                                        width = 2.dp,
                                    ),
                                    shape = RoundedCornerShape(5.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            TextField(
                                modifier = Modifier
                                    .focusRequester(focusRequesters[index])
                                    .onFocusChanged { focusState ->
                                        focusedIndex = if (focusState.isFocused) index else focusedIndex.takeIf { it != index } ?: -1
                                    },
                                value = value,
                                onValueChange = { newValue ->
                                    if (newValue.length == 1 && newValue.all { it.isDigit() }) {
                                        values = values.toMutableList().also { it[index] = newValue }

                                        if (index < values.size - 1) {
                                            focusRequesters[index + 1].requestFocus()
                                        } else {
                                            focusManager.clearFocus()
                                        }
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = TextStyle(
                                    fontSize = 25.sp,
                                    color = Color(0xFF000000),
                                    textAlign = TextAlign.Center
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    cursorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}