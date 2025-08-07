package com.example.tradeapp.ui.navigations

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.tradeapp.ui.pages.FinalPaymentPage
import com.example.tradeapp.ui.pages.HomePage
import com.example.tradeapp.ui.pages.PaymentPage
import com.example.tradeapp.ui.pages.ProfilePage
import com.example.tradeapp.ui.pages.TopUpPage
import com.example.tradeapp.ui.pages.TradePage
import com.example.tradeapp.ui.pages.WalletPage
import com.example.tradeapp.utils.NamePage


@Composable
fun MainPageNavigation(
    navigator: NavHostController,
) {
    NavHost(
        navController = navigator,
        startDestination = NamePage.HOME
    ) {
        composable(
            NamePage.HOME, deepLinks = listOf(
            )
        ) {
            HomePage(
                navigation = navigator,

            )
        }
        composable(
            NamePage.WALLET, deepLinks = listOf(
            )
        ) {
            WalletPage(
                navigation = navigator,
            )
        }
        composable(
            NamePage.PROFILE, deepLinks = listOf(
            )
        ) {
            ProfilePage(
                navigation = navigator,
            )
        }
        composable(
            NamePage.TRADE, deepLinks = listOf(
            )
        ) {

            TradePage(
                navigation = navigator,
            )
        }

        composable(
            NamePage.TOP_UP, deepLinks = listOf(
            )
        ) {
            TopUpPage(
                navigation = navigator
            )
        }

        composable(
            NamePage.PAYMENT, deepLinks = listOf(
            )
        ) {
            PaymentPage(navigator)
        }


        composable(
            NamePage.FINAL_PAYMENT, deepLinks = listOf(
            )
        ) {
            FinalPaymentPage(navigator)
        }

    }
}