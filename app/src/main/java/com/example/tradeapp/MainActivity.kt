package com.example.tradeapp


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Scaffold
import com.example.tradeapp.ui.pages.ChartPage
import com.example.tradeapp.ui.theme.TradeAppTheme
import com.example.tradeapp.ui.tools.Gradient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true) // محتوای صفحه زیر نوار وضعیت می‌رود
        setContent {
            TradeAppTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Gradient()
                    Scaffold(
                        modifier = Modifier
                            .statusBarsPadding() // برای تنظیم فاصله از نوار وضعیت
                            .windowInsetsPadding(WindowInsets.navigationBars) // این خط جادوییه
                            .fillMaxSize()
                            .background(Color.Transparent)
                    ) {
//                        BaseNav()


                        ChartPage(navigation = rememberNavController(),"6dfbff52-894d-4c24-b129-34a73dafe1a0")
                    }
                }
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TradeAppTheme {
//        Greeting("Android")
//    }
//}