package com.example.tradeapp.ui.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tradeapp.ui.pages.CompleteProfile
import com.example.tradeapp.ui.pages.LoginPage
import com.example.tradeapp.utils.NamePage


@Composable
fun LoginNavigation(
//    rootNavController: NavHostController
) {
    val loginNavController = rememberNavController()

    NavHost(
        navController = loginNavController,
        startDestination = NamePage.LOGIN_FIRST
    ) {
        composable(route = NamePage.LOGIN_FIRST) {
            LoginPage()
        }
        composable(route = NamePage.LOGIN_COMPLETE) {
            CompleteProfile()
        }
    }
}

