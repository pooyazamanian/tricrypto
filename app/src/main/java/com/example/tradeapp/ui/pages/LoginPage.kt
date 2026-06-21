package com.example.tradeapp.ui.pages

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradeapp.ui.components.*
import com.example.tradeapp.viewmodel.LoginViewModel
import com.example.tradeapp.viewmodel.util.UiState

@Composable
fun LoginPage(
    loginViewModel: LoginViewModel,
    onNavigateToBasePage: () -> Unit
) {
    val state by loginViewModel.state.collectAsState()
    val context = LocalContext.current
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    val isLoading = state.authStatus is UiState.Loading

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            loginViewModel.clearToastMessage()
        }
    }

    LaunchedEffect(state.authStatus) {
        when (val status = state.authStatus) {
            is UiState.Success -> onNavigateToBasePage()
            is UiState.Error -> {
                Toast.makeText(context, status.message ?: "Error occurred", Toast.LENGTH_LONG).show()
                loginViewModel.clearAuthError()
            }
            else -> {}
        }
    }

    GlassBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (state.isSignUpMode) "Create Account" else "Welcome Back",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (state.isSignUpMode) 
                    "Enter your details to get started"
                else 
                    "Log in to continue trading",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            GlassCard {
                Column(modifier = Modifier.padding(20.dp)) {
                    GlassTextField(
                        value = emailValue,
                        onValueChange = { emailValue = it },
                        label = "Email Address",
                        placeholder = "e.g. name@example.com",
                        enabled = !isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    GlassTextField(
                        value = passwordValue,
                        onValueChange = { passwordValue = it },
                        label = "Password",
                        placeholder = "Enter your password",
                        enabled = !isLoading
                        // Note: In a real app, add password transformation here
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            GlassButton(
                text = if (state.isSignUpMode) "Sign Up" else "Log In",
                onClick = { loginViewModel.submit(emailValue, passwordValue) },
                isLoading = isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = { loginViewModel.toggleMode() },
                enabled = !isLoading
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.7f))) {
                            append(if (state.isSignUpMode) "Already have an account? " else "Don't have an account? ")
                        }
                        withStyle(style = SpanStyle(color = Color(0xFFE94560), fontWeight = FontWeight.Bold)) {
                            append(if (state.isSignUpMode) "Log In" else "Sign Up")
                        }
                    }
                )
            }
        }
    }
}
