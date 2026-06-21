package com.example.tradeapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.tradeapp.ui.components.*
import com.example.tradeapp.ui.navigations.MainPageNavigation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BasePage() {
    val mainNavController = rememberNavController()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    GlassBackground {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { scope.launch { sheetState.show() } },
                    containerColor = Color(0xFFE94560),
                    contentColor = Color.White,
                    shape = androidx.compose.foundation.shape.CircleShape,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add, 
                        contentDescription = "Trade",
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            topBar = {
                GlassTopBar()
            },
            bottomBar = {
                GlassBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        mainNavController.navigate(route) {
                            popUpTo(mainNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { padding ->
            if (sheetState.isVisible) {
                TradeCryptoBottomSheet(isSheetVisible = sheetState)
            }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
                    // We don't apply bottom padding here to let content flow behind the glass bar, 
                    // but pages must handle their own internal bottom padding to avoid being cut off.
            ) {
                MainPageNavigation(navigator = mainNavController)
            }
        }
    }
}
