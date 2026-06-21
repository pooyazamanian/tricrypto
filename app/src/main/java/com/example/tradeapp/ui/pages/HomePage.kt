package com.example.tradeapp.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.tradeapp.R
import com.example.tradeapp.ui.components.*
import com.example.tradeapp.utils.NamePage
import com.example.tradeapp.viewmodel.MarketTrendsViewModel
import com.example.tradeapp.viewmodel.WatchlistViewModel
import com.example.tradeapp.viewmodel.util.UiStateWithCatch
import com.example.tradeapp.viewmodel.util.dataOrCached

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HomePage(
    navigation: NavHostController,
    watchlistViewModel: WatchlistViewModel = hiltViewModel(),
    marketTrendsViewModel: MarketTrendsViewModel = hiltViewModel()
) {
    val watchlistState by watchlistViewModel.state.collectAsState()
    val marketTrendsState by marketTrendsViewModel.state.collectAsState()
    val assetsWatchlist = watchlistState.watchlistData.dataOrCached()
    val trends = marketTrendsState.trendsData.dataOrCached()
    val isLoadingMarketTrends = marketTrendsState.trendsData is UiStateWithCatch.Loading
    val isLoadingWatchlist = watchlistState.watchlistData is UiStateWithCatch.Loading
    
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        // Balance Section
        GlassCard(opacity = 0.15f) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "حساب شما",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "1,000,000,000",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تومان",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp).rotate(90f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "5,000 (100%)",
                        color = Color(0xFF4CAF50),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GlassButton(
                text = "برداشت",
                onClick = { },
                modifier = Modifier.weight(1f),
                containerColor = Color.White.copy(alpha = 0.1f),
                contentColor = Color.White
            )
            GlassButton(
                text = "پرداخت",
                onClick = { },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Market Trends
        Text(
            text = "ترند های امروز:",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (isLoadingMarketTrends && trends.isNullOrEmpty()) {
            CircularProgressIndicator(color = Color(0xFFE94560), modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            trends?.let { list ->
                items(list.size) { index ->
                    val trend = list[index]
                    TrendCard(
                        name = trend.asset?.name ?: "",
                        symbol = trend.asset?.symbol ?: "",
                        price = trend.price.toString()
                    ) {
                        navigation.navigate("${NamePage.CHART}/${trend.asset?.id}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Watchlist
        Text(
            text = "فهرست پیگیری های من:",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoadingWatchlist && assetsWatchlist.isNullOrEmpty()) {
            CircularProgressIndicator(color = Color(0xFFE94560), modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        assetsWatchlist?.forEach { item ->
            WatchlistItemCard(
                name = item.name,
                symbol = item.symbol,
                price = item.price.toString(),
                logoUrl = item.logoUrl
            ) {
                navigation.navigate("${NamePage.CHART}/${item.id}")
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
        
        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
private fun TrendCard(name: String, symbol: String, price: String, onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier.width(140.dp).clickable(onClick = onClick),
        opacity = 0.12f
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = symbol, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = name, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "$price $", color = Color(0xFFE94560), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun WatchlistItemCard(name: String, symbol: String, price: String, logoUrl: String?, onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        opacity = 0.1f
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Restore Image/Icon
            AsyncImage(
                model = logoUrl,
                contentDescription = name,
                modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.1f)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.test),
                error = painterResource(R.drawable.test)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = symbol, 
                    color = Color.White, 
                    fontSize = 17.sp, 
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = name, 
                    color = Color.White.copy(alpha = 0.6f), 
                    fontSize = 13.sp
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$price $", 
                    color = Color.White, 
                    fontSize = 17.sp, 
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
                Text(
                    text = "+2.45%", // Placeholder for trend value if needed
                    color = Color(0xFF4CAF50),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
