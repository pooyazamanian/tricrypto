package com.example.tradeapp.ui.navigations

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tradeapp.ui.pages.CompleteProfile
import com.example.tradeapp.ui.pages.LoginPage
import com.example.tradeapp.utils.NamePage
import com.example.tradeapp.viewmodel.LoginViewModel
import com.example.tradeapp.viewmodel.WatchlistViewModel


@Composable
fun LoginNavigation(
    loginViewModel: LoginViewModel,
) {
    val loginNavController = rememberNavController()

    NavHost(
        navController = loginNavController,
        startDestination = NamePage.LOGIN_FIRST
    ) {
        composable(route = NamePage.LOGIN_FIRST) {
            LoginPage(loginViewModel){}
        }
        composable(route = NamePage.LOGIN_COMPLETE) {
            CompleteProfile()
        }
    }
}

