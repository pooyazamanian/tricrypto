package com.example.tradeapp.ui.navigations

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.tradeapp.ui.pages.HomePage
import com.example.tradeapp.ui.pages.ProfilePage
import com.example.tradeapp.ui.pages.TopUpPage
import com.example.tradeapp.ui.pages.TradePage
import com.example.tradeapp.ui.pages.WalletPage
import com.example.tradeapp.utils.NamePage


@Composable
fun MainPageNavigation(
    navigator: NavHostController,
    paddingValues: PaddingValues,
) {
    NavHost(
        navController = navigator,
        startDestination = NamePage.HOME
    ) {
        composable(
            NamePage.HOME, deepLinks = listOf(
            )
        ) {
            LaunchedEffect(Unit) {
                println("HomePage Loaded!")
            }
            HomePage(
                navigation = navigator,
                padding = paddingValues,
            )
        }
        composable(
            NamePage.WALLET, deepLinks = listOf(
            )
        ) {
            LaunchedEffect(Unit) {
                println("HomePage Loaded!")
            }
            WalletPage(
                navigation = navigator,
                padding = paddingValues,
            )
        }
        composable(
            NamePage.PROFILE, deepLinks = listOf(
            )
        ) {
            LaunchedEffect(Unit) {
                println("HomePage Loaded!")
            }
            ProfilePage(
                navigation = navigator,
                padding = paddingValues,
            )
        }
        composable(
            NamePage.TRADE, deepLinks = listOf(
            )
        ) {
            LaunchedEffect(Unit) {
                println("HomePage Loaded!")
            }
            TradePage(
                navigation = navigator,
                padding = paddingValues,
            )
        }

        composable(
            NamePage.PAYMENT, deepLinks = listOf(
            )
        ) {
            LaunchedEffect(Unit) {
                println("HomePage Loaded!")
            }
            TopUpPage(
                navigation = navigator,
                padding = paddingValues,
            )
        }

    }
}