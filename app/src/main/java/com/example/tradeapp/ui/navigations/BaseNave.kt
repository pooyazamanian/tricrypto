package com.example.tradeapp.ui.navigations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.hilt.navigation.compose.*
import com.example.tradeapp.damin.session.AuthState
import com.example.tradeapp.ui.BasePage
import com.example.tradeapp.ui.components.NetWorkConnectionDialog
import com.example.tradeapp.ui.pages.SplashPage
import com.example.tradeapp.utils.NetworkObserver
import com.example.tradeapp.viewmodel.AuthViewModel
import com.example.tradeapp.viewmodel.LoginViewModel


@Composable
fun BaseNav(
    loginViewModel: LoginViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val networkObserver = NetworkObserver(LocalContext.current)
    val isConnected by networkObserver.isConnected.collectAsState(initial = true)
    val showLostConnection = remember { mutableStateOf(false) }

    LaunchedEffect(isConnected) {
        showLostConnection.value = !isConnected
    }

    if (showLostConnection.value) {
        NetWorkConnectionDialog()
    }

    val authState by authViewModel.authState.collectAsState()

    AnimatedContent(
        targetState = authState,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
        },
        label = "PageTransition"
    ) { state ->
        when (state) {
            is AuthState.Loading -> SplashPage()
            is AuthState.Authenticated -> BasePage()
            is AuthState.Unauthenticated -> LoginNavigation(loginViewModel)
            is AuthState.SessionExpired -> LoginNavigation(loginViewModel)
            is AuthState.Error -> LoginNavigation(loginViewModel)
        }
    }
}