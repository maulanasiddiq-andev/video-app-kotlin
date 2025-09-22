package com.example.videoapp.pages.auth

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.videoapp.repositories.AuthRepository
import com.example.videoapp.viewModels.auth.rememberRegisterViewModel
import kotlinx.coroutines.delay
import kotlin.collections.listOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun OtpVerificationScreen(navHostController: NavHostController, repository: AuthRepository) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val countDownSeconds: Duration = (60 * 15).seconds
    var isTimerRunning by remember { mutableStateOf(true) }
    var timeLeft by remember { mutableStateOf(countDownSeconds) }

    var values by remember { mutableStateOf(listOf("", "", "", "")) }
    val focusRequesters = remember {
        listOf(FocusRequester(), FocusRequester(), FocusRequester(), FocusRequester())
    }
    var focusedIndex by remember { mutableIntStateOf(-1) }

    val viewModel = rememberRegisterViewModel(navHostController, repository = repository)
    val isLoading = viewModel.isLoadingOtp.value
    val succeed = viewModel.otpSucceed.value
    val message = viewModel.message.value

    LaunchedEffect(message, succeed) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }

        if (succeed) {
            navHostController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit, isTimerRunning) {
        while (timeLeft > 0.seconds && isTimerRunning) {
            delay(1.seconds)
            timeLeft -= 1.seconds
        }
    }

    fun submit() {
        isTimerRunning = false

        var value = ""
        values.forEach {
            value += it
        }

        viewModel.checkOtpValidation(value)
    }

    fun resendOTP() {
        isTimerRunning = false

        timeLeft = countDownSeconds
        isTimerRunning = true
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
                Text(
                    "Kode OTP sudah dikirim ke email@example.com",
                    style = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(30.dp))
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
                                            submit()
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
                Spacer(modifier = Modifier.height(40.dp))
                val formatted = "%02d:%02d".format(timeLeft.inWholeMinutes, timeLeft.inWholeSeconds % 60)
                Text(
                    formatted,
                    style = TextStyle(
                        fontSize = 20.sp
                    )
                )
                Spacer(modifier = Modifier.height(40.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        "Belum menerima kode OTP?",
                        style = TextStyle(
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        "Kirim ulang",
                        modifier = Modifier.clickable{resendOTP()},
                        style = TextStyle(
                            color = Color(0xFF2196f3),
                            fontSize = 16.sp
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
                            color = Color(0xffffffff),
                            modifier = Modifier.size(17.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Text(
                            "Kirim",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}