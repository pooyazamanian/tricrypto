package com.example.tradeapp.ui.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tradeapp.ui.theme.darkGray
import com.example.tradeapp.ui.tools.Gradient
import com.example.tradeapp.utils.NamePage
import com.example.tradeapp.utils.sealedClasses.AuthResponse
import com.example.tradeapp.viewmodel.LoginViewModel
import com.example.tradeapp.viewmodel.util.UiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@Composable
private fun RegisterHeader() {
    Text(
        text = "Create An Account",
        style = MaterialTheme.typography.titleLarge,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Enter your personal data to create an account",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White
    )
}
@Composable
fun LoginPage(
    loginViewModel: LoginViewModel,
    onNavigateToBasePage: () -> Unit
) {
    // گرفتن State یکپارچه
    val state by loginViewModel.state.collectAsState()
    val context = LocalContext.current

    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    val isLoading = state.authStatus is UiState.Loading

    // 1. کنترل Toastهای عمومی (مثل ثبت‌نام موفق یا خالی بودن فیلدها)
    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            loginViewModel.clearToastMessage() // مصرف کردن پیام
        }
    }

    // 2. کنترل وضعیت نتورک لاگین (Success و Error)
    LaunchedEffect(state.authStatus) {
        when (val status = state.authStatus) {
            is UiState.Success -> {
                // کاربر لاگین شده، انتقال به صفحه اصلی
                onNavigateToBasePage()
            }
            is UiState.Error -> {
                // ارور زمان ریکوئست
                Toast.makeText(context, status.message ?: "ارور نامشخص", Toast.LENGTH_LONG).show()
                loginViewModel.clearAuthError() // مصرف کردن ارور
            }
            else -> {} // Idle یا Loading کاری توی LaunchedEffect ندارن
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Gradient()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 110.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterHeader()
            Spacer(modifier = Modifier.height(40.dp))

            // -- فیلد ایمیل --
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = "Email", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = emailValue,
                    onValueChange = { emailValue = it },
                    placeholder = { Text("john.doe@example.com", color = Color.White.copy(0.7f)) },
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.DarkGray, unfocusedContainerColor = Color.DarkGray
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // -- فیلد پسورد --
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = "Password", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = passwordValue,
                    onValueChange = { passwordValue = it },
                    placeholder = { Text("Enter your password", color = Color.White.copy(0.7f)) },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.DarkGray, unfocusedContainerColor = Color.DarkGray
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(35.dp))

            // -- دکمه اصلی لاگین/ثبت نام --
            Button(
                onClick = { loginViewModel.submit(emailValue, passwordValue) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Black, strokeWidth = 2.dp)
                } else {
                    Text(text = if (state.isSignUpMode) "Sign up" else "Log in", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // -- تغییر حالت (ورود / ثبت‌نام) --
            TextButton(
                onClick = { loginViewModel.toggleMode() },
                enabled = !isLoading
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Light, color = Color.White.copy(0.8f))) {
                            append(if (state.isSignUpMode) "Already have an account? " else "Don't have an account? ")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
                            append(if (state.isSignUpMode) "Log in" else "Sign up")
                        }
                    }
                )
            }
        }
    }
}




//            GoogleSignInButton(
//                onClick = {
//                    authManager.loginGoogleUser()
//                        .onEach { result ->
//                            if (result is AuthResponse.Success) {
//                                Log.d("auth", "Google Success")
//                            } else {
//                                Log.e("auth", "Google Failed")
//                            }
//                        }
//                        .launchIn(coroutineScope)
//                }
//            )

//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.padding(vertical = 30.dp)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(1.dp)
//                        .background(Color.White.copy(alpha = 0.2f))
//                )
//
//                Text(
//                    text = "Or",
//                    color = Color.White.copy(alpha = 0.7f),
//                    modifier = Modifier.padding(horizontal = 10.dp)
//                )
//
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(1.dp)
//                        .background(Color.White.copy(alpha = 0.2f))
//                )
//            }
