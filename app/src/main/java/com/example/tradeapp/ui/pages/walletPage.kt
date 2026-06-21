package com.example.tradeapp.ui.pages

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.tradeapp.R
import com.example.tradeapp.ui.components.*
import com.example.tradeapp.viewmodel.AssetViewModel
import com.example.tradeapp.viewmodel.MarketTrendsViewModel
import com.example.tradeapp.viewmodel.UserAssetViewModel
import com.example.tradeapp.viewmodel.intent.AssetIntent
import com.example.tradeapp.viewmodel.intent.UserAssetIntent
import com.example.tradeapp.viewmodel.util.dataOrCached

@Composable
fun WalletPage(
    navigation: NavHostController,
    userAssetViewModel: UserAssetViewModel = hiltViewModel(),
    assetViewModel: AssetViewModel = hiltViewModel(),
    marketTrendsViewModel: MarketTrendsViewModel = hiltViewModel()
) {
    val userAssetState by userAssetViewModel.state.collectAsState()
    val assetState by assetViewModel.state.collectAsState()
    val marketTrendsState by marketTrendsViewModel.state.collectAsState()
    
    val trends = marketTrendsState.trendsData.dataOrCached()

    LaunchedEffect(Unit) {
        assetViewModel.handleIntent(AssetIntent.LoadAssets)
        userAssetViewModel.handleIntent(UserAssetIntent.LoadUserAssets)
    }

    val totalBalanceUsd = remember(userAssetState.userAssets, trends) {
        userAssetState.userAssets.sumOf { userAsset ->
            val trend = trends?.find { it.asset?.id == userAsset.assetId }
            (userAsset.quantity ?: 0.0) * (trend?.price ?: 0.0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                WalletHeader(totalBalanceUsd)
            }

            item {
                PortfolioBreakdown(userAssetState.userAssets, trends)
            }

            item {
                Text(
                    text = "دارایی‌های شما",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            itemsIndexed(userAssetState.userAssets) { index, userAsset ->
                val assetInfo = assetState.assets.find { it.id == userAsset.assetId }
                val trend = trends?.find { it.asset?.id == userAsset.assetId }
                
                val animState = remember { MutableTransitionState(false).apply { targetState = true } }
                AnimatedVisibility(
                    visibleState = animState,
                    enter = fadeIn(animationSpec = tween(300, delayMillis = index * 50)) + 
                            slideInVertically(animationSpec = tween(300, delayMillis = index * 50)) { it / 2 }
                ) {
                    AssetWalletCard(
                        name = assetInfo?.name ?: "نامشخص",
                        symbol = assetInfo?.symbol ?: "",
                        amount = userAsset.quantity ?: 0.0,
                        price = trend?.price ?: 0.0,
                        logoUrl = assetInfo?.logoUrl,
                        onClick = { 
                            assetInfo?.id?.let { id ->
                                navigation.navigate("chart/$id")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WalletHeader(balanceUsd: Double) {
    GlassCard(opacity = 0.15f) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "موجودی کل کل",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$${String.format("%.2f", balanceUsd)}",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "≈ ${String.format("%,d", (balanceUsd * 70000).toLong())} تومان",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ActionButton(
                    text = "واریز",
                    icon = Icons.Default.KeyboardArrowDown,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "برداشت",
                    icon = Icons.Default.KeyboardArrowUp,
                    color = Color(0xFFE94560),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun PortfolioBreakdown(userAssets: List<com.example.tradeapp.dto.UserAsset>, trends: List<com.example.tradeapp.models.MarketTrend>?) {
    GlassCard(opacity = 0.1f) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val totalValue = userAssets.sumOf { ua -> 
                val price = trends?.find { it.asset?.id == ua.assetId }?.price ?: 0.0
                (ua.quantity ?: 0.0) * price
            }
            
            if (totalValue > 0) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "توزیع دارایی‌ها",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        userAssets.take(4).forEachIndexed { index, ua ->
                            val price = trends?.find { it.asset?.id == ua.assetId }?.price ?: 0.0
                            val weight = (((ua.quantity ?: 0.0) * price) / totalValue).toFloat()
                            val color = when(index) {
                                0 -> Color(0xFFE94560)
                                1 -> Color(0xFF4CAF50)
                                2 -> Color(0xFF2196F3)
                                else -> Color(0xFFFFC107)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(maxOf(0.01f, weight))
                                    .background(color)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "هنوز دارایی ندارید",
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun AssetWalletCard(
    name: String,
    symbol: String,
    amount: Double,
    price: Double,
    logoUrl: String?,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        opacity = 0.12f
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = logoUrl,
                contentDescription = name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.test),
                error = painterResource(R.drawable.test)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = symbol,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = name,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format("%.4f", amount),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${String.format("%.2f", amount * price)}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(44.dp)
            .clickable { },
        shape = CircleShape,
        color = color.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
