package com.example.videoapp.pages.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.videoapp.components.AuthInputComponent
import com.example.videoapp.viewModels.auth.LoginViewModel
import android.graphics.Color as AndroidColor

@Composable
fun LoginScreen(navHostController: NavHostController, viewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val secondFocusRequester = remember { FocusRequester() }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading = viewModel.isLoading.value
    val message by viewModel.message

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    fun submit() {
        viewModel.login(email, password)
    }

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
                    "Login",
                    style = TextStyle(
                        fontSize = 60.sp,
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
                    .padding(20.dp, 0.dp)
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(10.dp, RoundedCornerShape(10.dp))
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    AuthInputComponent(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Email",
                        modifier = Modifier,
                        keyboardAction = KeyboardActions(
                            onNext = {
                                secondFocusRequester.requestFocus()
                            }
                        ),
                        keyBoardType = KeyboardType.Email
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp
                    )
                    AuthInputComponent(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Password",
                        modifier = Modifier.focusRequester(secondFocusRequester),
                        isPassword = true,
                        isLast = true,
                        keyboardAction = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                submit()
                            }
                        )
                    )
                }
                Spacer(modifier = Modifier.height(60.dp))
                Box(
                    modifier = Modifier
                        .clickable{submit()}
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF2196f3))
                        .padding(15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color(AndroidColor.WHITE),
                            modifier = Modifier.size(17.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Text(
                            "Login",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Belum punya akun?")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        "Daftar",
                        style = TextStyle(
                            color = Color(0xFF2196f3)
                        ),
                        modifier = Modifier
                            .clickable{
                                navHostController.navigate("register") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                    )
                }
            }
        }
    }
}