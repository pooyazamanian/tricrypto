package com.example.tradeapp.ui

import android.annotation.*
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.example.tradeapp.ui.navigations.MainPageNavigation
import com.example.tradeapp.ui.tools.BottomBar
import com.example.tradeapp.ui.tools.TopBar
import com.example.tradeapp.ui.tools.TradeCryptoBottomSheet
import com.example.tradeapp.viewmodel.TradeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter",
    "UseOfNonLambdaOffsetOverload",
    "UnrememberedMutableState", "SuspiciousIndentation"
)
@Composable
fun BasePage(
    viewModel: TradeViewModel = hiltViewModel()
) {


    val mainNavController = rememberNavController()
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val scope = rememberCoroutineScope()
//    NavDrawer(
//        drawerState = drawerState,
////         baseNavController = baseNavController,
//        navigation = mainNavController
//    ){
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)


    val hasScrolled = remember {
        derivedStateOf { scrollState.value > 0 }
    }
        Scaffold(modifier = Modifier.fillMaxSize(),
            contentColor = Color.Transparent,
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    scope.launch {
                        sheetState.show()
                    }

                }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            },
            topBar = {
            // استخراج route فعلی
            Crossfade(
                targetState = true, label = "TopBarFade"
            ) {
                showTopBar ->
                if (showTopBar) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                    ) {
                        TopBar(
                            mainNavController = mainNavController
                        )
                        Spacer(Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(1.dp).shadow( if (hasScrolled.value) 4.dp else 0.dp))
                    }

                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                    ) {
                    }
                }
            }
        },
            bottomBar = {
                Crossfade(
                    targetState = true, label = "TopBarFade"
                ) { showTopBar ->
                    if (showTopBar) {
                        BottomBar(
                            mainNavController = mainNavController
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(106.dp)
                        ) {
                        }
                    }
                }
            }) { padding ->

            if (sheetState.isVisible) {
                TradeCryptoBottomSheet(
                    isSheetVisible = sheetState,
                    viewModel = viewModel
                )
            }
            Column(
                modifier = Modifier.padding(top = padding.calculateTopPadding()).fillMaxSize().verticalScroll(scrollState)
            ) {
                MainPageNavigation(
                    navigator = mainNavController,
                )
                Spacer(Modifier.height(padding.calculateBottomPadding() + 10.dp))

            }

        }


//    }

}