package com.example.tradeapp.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tradeapp.R
import com.example.tradeapp.dto.Profile
import com.example.tradeapp.viewmodel.ProfileEditorViewModel
import com.example.tradeapp.viewmodel.ProfileUiEffect
import com.example.tradeapp.viewmodel.intent.ProfileEditorIntent
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileEditorPage(
    navigation: NavHostController,
    profile: Profile,
    user: UserInfo,
    viewModel: ProfileEditorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProfileEditorIntent.ChangeProfileField(profile))
        viewModel.handleIntent(ProfileEditorIntent.ChangeUserInfoField(user))
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ProfileUiEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is ProfileUiEffect.SaveSuccess -> {
                    snackbarHostState.showSnackbar("تغییرات با موفقیت ذخیره شد")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A1A2E),
                            Color(0xFF16213E),
                            Color(0xFF0F3460)
                        )
                    )
                )
        ) {
            LiquidBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(160.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                        .border(2.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Image(
                        painter = painterResource(R.drawable.test),
                        contentDescription = "profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "ویرایش حساب کاربری",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                GlassCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ModernInput(
                            label = "ایمیل",
                            value = state.user?.email ?: "",
                            enabled = false,
                            onValueChange = {}
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ModernInput(
                            label = "نام کامل",
                            value = state.profile?.fullName ?: "",
                            onValueChange = {
                                state.profile?.let { p ->
                                    viewModel.handleIntent(ProfileEditorIntent.ChangeProfileField(p.copy(fullName = it)))
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GlassCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ModernInput(
                            label = "نام کاربری",
                            value = state.profile?.username ?: "",
                            onValueChange = {
                                state.profile?.let { p ->
                                    viewModel.handleIntent(ProfileEditorIntent.ChangeProfileField(p.copy(username = it)))
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ModernInput(
                            label = "شماره تلفن",
                            value = state.user?.phone ?: "",
                            onValueChange = {
                                state.user?.let { u ->
                                    viewModel.handleIntent(ProfileEditorIntent.ChangeUserInfoField(u.copy(phone = it)))
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GlassCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ModernInput(
                            label = "کد ملی",
                            value = state.profile?.nationalId ?: "",
                            onValueChange = {
                                state.profile?.let { p ->
                                    viewModel.handleIntent(ProfileEditorIntent.ChangeProfileField(p.copy(nationalId = it)))
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        state.profile?.let {
                            viewModel.handleIntent(ProfileEditorIntent.SendProfileDate(it))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE94560)
                    ),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("تایید و ذخیره", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun GlassCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
    ) {
        content()
    }
}

@Composable
fun ModernInput(
    label: String,
    value: String,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color(0xFFE94560),
                focusedBorderColor = Color(0xFFE94560),
                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                focusedContainerColor = Color.White.copy(alpha = 0.08f),
                disabledBorderColor = Color.White.copy(alpha = 0.1f),
                disabledTextColor = Color.White.copy(alpha = 0.4f)
            ),
            singleLine = true
        )
    }
}

@Composable
fun LiquidBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color(0xFFE94560).copy(alpha = 0.15f),
            radius = 400f,
            center = androidx.compose.ui.geometry.Offset(x = 0f, y = 0f)
        )
        drawCircle(
            color = Color(0xFF0F3460).copy(alpha = 0.3f),
            radius = 600f,
            center = androidx.compose.ui.geometry.Offset(x = size.width, y = size.height * 0.7f)
        )
    }
}
