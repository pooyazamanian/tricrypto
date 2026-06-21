package com.example.tradeapp.ui.pages

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tradeapp.R
import com.example.tradeapp.ui.components.*
import com.example.tradeapp.utils.NamePage
import com.example.tradeapp.viewmodel.ProfileViewModel
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ProfilePage(
    navigation: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val state by profileViewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isLoading) {
            ProfileShimmer()
        } else {
            // Profile Header Card
            val headerAnimState = remember { MutableTransitionState(false).apply { targetState = true } }
            AnimatedVisibility(
                visibleState = headerAnimState,
                enter = fadeIn(animationSpec = tween(600)) + expandVertically(animationSpec = tween(600))
            ) {
                GlassCard(opacity = 0.15f) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.test),
                            contentDescription = "profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                        )
                        Spacer(Modifier.width(20.dp))
                        Column {
                            Text(
                                text = state.profile?.fullName ?: "نام کاربر",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = state.user?.email ?: "ایمیل یافت نشد",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Personal Info Card
            val infoAnimState = remember { MutableTransitionState(false).apply { targetState = true } }
            AnimatedVisibility(
                visibleState = infoAnimState,
                enter = fadeIn(animationSpec = tween(600, delayMillis = 200)) + slideInVertically(animationSpec = tween(600, delayMillis = 200)) { it / 2 }
            ) {
                GlassCard(opacity = 0.12f) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRow(title = "نام کاربری", value = state.profile?.username ?: "-")
                        Divider(color = Color.White.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 12.dp))
                        InfoRow(title = "شماره تلفن", value = state.user?.phone ?: "-")
                        Divider(color = Color.White.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 12.dp))
                        InfoRow(title = "کد ملی", value = state.profile?.nationalId ?: "-")
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Actions
            if (state.profile != null && state.user != null) {
                val actionsAnimState = remember { MutableTransitionState(false).apply { targetState = true } }
                AnimatedVisibility(
                    visibleState = actionsAnimState,
                    enter = fadeIn(animationSpec = tween(600, delayMillis = 400))
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        GlassButton(
                            text = "ویرایش پروفایل",
                            onClick = {
                                val profileJson = Json.encodeToString(state.profile)
                                val profile = URLEncoder.encode(profileJson, StandardCharsets.UTF_8.toString())
                                val userJson = Json.encodeToString(state.user)
                                val user = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
                                navigation.navigate("${NamePage.PROFILE_EDITOR}/${profile}/${user}")
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        GlassButton(
                            text = "خروج از حساب",
                            onClick = { profileViewModel.logout() },
                            containerColor = Color.White.copy(alpha = 0.1f),
                            contentColor = Color(0xFFE94560)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
private fun ProfileShimmer() {
    Column(modifier = Modifier.fillMaxWidth()) {
        GlassShimmer(modifier = Modifier.fillMaxWidth().height(140.dp))
        Spacer(modifier = Modifier.height(20.dp))
        GlassShimmer(modifier = Modifier.fillMaxWidth().height(200.dp))
        Spacer(modifier = Modifier.height(32.dp))
        GlassShimmer(modifier = Modifier.fillMaxWidth().height(56.dp))
        Spacer(modifier = Modifier.height(16.dp))
        GlassShimmer(modifier = Modifier.fillMaxWidth().height(56.dp))
    }
}

@Composable
private fun InfoRow(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun Divider(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color)
    )
}
