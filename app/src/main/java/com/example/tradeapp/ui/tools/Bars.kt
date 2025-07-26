package com.example.tradeapp.ui.tools

import android.annotation.*
import android.content.*
import android.net.*
import android.provider.Settings.*
import android.util.*
import androidx.activity.compose.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import com.example.tradeapp.R
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.layout.ContentScale.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.unit.LayoutDirection.*
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import coil3.compose.*
import com.example.tradeapp.ui.theme.BottomNavigationItem
import com.example.tradeapp.utils.NamePage
import kotlinx.coroutines.*
import java.net.*
import java.nio.charset.*

@SuppressLint("SuspiciousIndentation")
@Composable
fun TopBar(
    mainNavController: NavHostController,
//    badge: MutableState<Int>,
//    onNotificationClick: () -> Unit,
//    onAccountClick: () -> Unit,
//    onMenuClick: () -> Unit,
//    onImageClick: () -> Unit,
//    onSearchClick: () -> Unit
) {
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
            Image(
                painter = painterResource(R.drawable.menu),
                contentDescription = "Back",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {

                    },
            )
            Image(
                    painter = painterResource(id = R.drawable.title),
                    contentDescription = "profile",
                    modifier = Modifier
                        .wrapContentWidth(),
                    contentScale = ContentScale.Crop
                )
            Image(
                painter = painterResource(R.drawable.setting),
                contentDescription = "Back",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {

                    },
            )
    }
}


//fun isAnimationDisabled(context: Context): Boolean {
//    val animationScale = Global.getFloat(
//        context.contentResolver,
//        Global.ANIMATOR_DURATION_SCALE,
//        1.0f // مقدار پیش‌فرض
//    )
//    return animationScale == 0f
//}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun BottomBar(
    mainNavController: NavHostController
) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp - 10.dp
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val listMenuItem = listOf(
        BottomNavigationItem(
            title = "Home",
            unSelectedIcon =R.drawable.home_uns,
            SelectedIcon = R.drawable.home_s,
            rout = NamePage.HOME,
            onClick = {
                mainNavController.navigate(NamePage.HOME) {
                    popUpTo(NamePage.HOME) {
                        inclusive = true
                    }
                }
            }
        ),

        BottomNavigationItem(
            title = "Trade",
            unSelectedIcon =R.drawable.trade_uns,
            SelectedIcon =R.drawable.trade_s,
            rout = NamePage.TRADE,
            onClick = {
                mainNavController.navigate(NamePage.TRADE) {
                    popUpTo(NamePage.TRADE) {
                        inclusive = true
                    }
                }
            }),
        BottomNavigationItem(
            title = "Top Up",
            unSelectedIcon =R.drawable.plus_uns,
            SelectedIcon =R.drawable.plus_s,
            rout = NamePage.PAYMENT,
            onClick = {
                mainNavController.navigate(NamePage.PAYMENT) {
                    popUpTo(NamePage.PAYMENT) {
                        inclusive = true
                    }
                }
            }
        ),


        BottomNavigationItem(
            title = "Wallet",
            unSelectedIcon =R.drawable.wallet,
            SelectedIcon =R.drawable.wallet_s,
            rout = NamePage.WALLET,
            onClick = {
                mainNavController.navigate(NamePage.WALLET) {
                    popUpTo(NamePage.WALLET) {
                        inclusive = true
                    }
                }
            }
        ),
        BottomNavigationItem(
            title = "Profile",
            unSelectedIcon =R.drawable.profile_uns,
            SelectedIcon =R.drawable.profile_s,
            rout = NamePage.PROFILE,
            onClick = {
                mainNavController.navigate(NamePage.PROFILE) {
                    popUpTo(NamePage.PROFILE) {
                        inclusive = true
                    }
                }

            }
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(106.dp)
            .background(color = Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.padding(5.dp)
                .fillMaxWidth()
                .height(90.dp).clip(RoundedCornerShape(25.dp))
//                .shadow(15.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(start = 2.dp, end = 2.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listMenuItem.forEach { item ->
                Column(
                    modifier = Modifier
                        .clickable {
//                            selected == item.Rout
                            if (currentRoute != item.rout) {
                                item.onClick()
                            }


                        }
                        .padding(top = 10.dp)
                        .fillMaxHeight()
                        .width(screenWidthDp / 5),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    if (item.unSelectedIcon != null && item.SelectedIcon != null) {
                        Image(
                            painter = painterResource(if (currentRoute == item.rout) item.SelectedIcon else item.unSelectedIcon),
                            contentDescription = item.title,
                            modifier = Modifier
                                .size(32.dp)
                                .padding(bottom = 5.dp),
//                            tint = if (mainNavController.currentDestination?.route == item.rout) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Row(
                            modifier = Modifier
                                .size(32.dp)
                                .padding(bottom = 5.dp)
                        ) {

                        }
                    }

                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (mainNavController.currentDestination?.route == item.rout) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.inverseSurface
                    )
                }

            }
        }

    }


}