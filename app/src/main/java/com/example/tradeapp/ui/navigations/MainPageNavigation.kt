package com.example.tradeapp.ui.navigations

import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.tradeapp.damin.model.Profile
import com.example.tradeapp.ui.pages.ChartPage
import com.example.tradeapp.ui.pages.FinalPaymentPage
import com.example.tradeapp.ui.pages.HomePage
import com.example.tradeapp.ui.pages.PaymentPage
import com.example.tradeapp.ui.pages.ProfileEditorPage
import com.example.tradeapp.ui.pages.ProfilePage
import com.example.tradeapp.ui.pages.TopUpPage
import com.example.tradeapp.ui.pages.TradePage
import com.example.tradeapp.ui.pages.WalletPage
import com.example.tradeapp.utils.NamePage
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets


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
            NamePage.PROFILE, deepLinks = listOf(
            )
        ) {
            ProfilePage(
                navigation = navigator
            )
        }

        composable(
            "${NamePage.PROFILE_EDITOR}/{profileJson}/{userJson}",
            arguments = listOf(
                navArgument("profileJson") { type = NavType.StringType },
                navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val profileEncoded = backStackEntry.arguments?.getString("profileJson")
            val profileJson = java.net.URLDecoder.decode(profileEncoded, StandardCharsets.UTF_8.toString())
            val profile = Json.decodeFromString<Profile>(profileJson)
            val userEncoded = backStackEntry.arguments?.getString("userJson")
            val userJson = java.net.URLDecoder.decode(userEncoded, StandardCharsets.UTF_8.toString())
            val user = Json.decodeFromString<UserInfo>(userJson)
            ProfileEditorPage(
                navigation = navigator,
                user = user,
                profile = profile
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


//        composable(
//            NamePage.CHART, deepLinks = listOf(
//            )
//        ) {
//            ChartPage(navigator)
//        }
//        composable(
//            NamePage.WALLET, deepLinks = listOf(
//            )
//        ) {
//            WalletPage(
//                navigation = navigator,
//            )
//        }

        composable(
            NamePage.WALLET, deepLinks = listOf(
            )
        ) {
            WalletPage(navigator)
        }
    }
}