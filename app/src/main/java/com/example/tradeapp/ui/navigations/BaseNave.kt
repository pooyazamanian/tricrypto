package com.example.tradeapp.ui.navigations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.hilt.navigation.compose.*
import androidx.navigation.compose.composable
import com.example.tradeapp.ui.BasePage
import com.example.tradeapp.ui.pages.ChartPage
import com.example.tradeapp.ui.pages.SplashPage
import com.example.tradeapp.ui.tools.NetWorkConnectionDialog
import com.example.tradeapp.utils.NamePage
import com.example.tradeapp.utils.NetworkObserver
import com.example.tradeapp.viewmodel.LoginViewModel
import com.example.tradeapp.viewmodel.WatchlistViewModel
import com.example.tradeapp.viewmodel.util.dataOrCached


@Composable
fun BaseNav(
    loginViewModel: LoginViewModel = hiltViewModel(),
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

    // گرفتن State کامل
    val state by loginViewModel.state.collectAsState()

    AnimatedContent(
        targetState = state.currentPage, // مستقیم از currentPage استفاده می‌کنیم
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
        },
        label = "PageTransition"
    ) { targetPage ->
        when (targetPage) {
            NamePage.SPLASHSCREEN -> SplashPage()
            NamePage.LOGIN -> LoginNavigation(loginViewModel)
            NamePage.BASE_PAGE -> BasePage()
        }
    }
}