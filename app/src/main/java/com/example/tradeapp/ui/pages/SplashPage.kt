package com.example.tradeapp.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import coil3.compose.*
import com.example.tradeapp.R
import coil3.gif.*
import coil3.request.*
import com.example.tradeapp.ui.tools.Gradient
import com.example.tradeapp.viewmodel.LoginViewModel
import io.github.jan.supabase.auth.auth

@Composable
fun SplashPage(loginViewModel: LoginViewModel) {


    val isLoggedIn = remember { mutableStateOf(false) }
    val supabase = loginViewModel.getSupaBase()

    LaunchedEffect(Unit) {
        val session = supabase.auth.currentSessionOrNull()
        isLoggedIn.value = session != null
        if (session != null) {
            println("کاربر لاگین شده است، توکن: ${session.accessToken}")
        } else {
            println("کاربر لاگین نشده است")
        }
    }

    if (isLoggedIn.value) {
        Text("کاربر لاگین شده است")
    } else {
        Text("کاربر لاگین نشده است")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Gradient()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(R.drawable.splash)
                    .crossfade(false).repeatCount(0).build(),
                contentDescription = "Splash Animation",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
            )
//        if(baseViewModel.isVersionLoaded.value && baseViewModel.version.value ==null){
//            Column (modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(Icons.Default.Refresh, contentDescription = "refresh", modifier = Modifier
//                    .height(50.dp)
//                    .clickable {
//                        baseViewModel.isVersionLoaded.value = false
//                        baseViewModel.fetch()
//                    }, tint = Color.White)
//                Text(
//                    text = "لطفا دوباره تلاش کنید",
//                    fontSize = 12.sp,
//                    textAlign = TextAlign.Center,
//                    color = Color.White,
//                    modifier = Modifier.padding(5.dp)
//                )
//            }
//        }else{
//            Column (modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//            }
//        }
        }
    }
}