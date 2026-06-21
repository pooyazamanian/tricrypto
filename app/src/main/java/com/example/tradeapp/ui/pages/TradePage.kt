package com.example.tradeapp.ui.pages

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tradeapp.R
import com.example.tradeapp.ui.components.*
import com.example.tradeapp.viewmodel.MarketTrendsViewModel
import com.example.tradeapp.viewmodel.OrderViewModel
import com.example.tradeapp.viewmodel.TradeListViewModel
import com.example.tradeapp.viewmodel.intent.OrderIntent
import com.example.tradeapp.viewmodel.intent.TradeListIntent
import com.example.tradeapp.viewmodel.state.OrderType
import com.example.tradeapp.viewmodel.util.dataOrCached

@Composable
fun TradePage(
    navigation: NavHostController,
    tradeViewModel: TradeListViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
    marketTrendsViewModel: MarketTrendsViewModel = hiltViewModel()
) {
    val tradeState by tradeViewModel.state.collectAsState()
    val orderState by orderViewModel.state.collectAsState()
    val marketTrendsState by marketTrendsViewModel.state.collectAsState()
    
    val trends = marketTrendsState.trendsData.dataOrCached()

    LaunchedEffect(Unit) {
        tradeViewModel.handleIntent(TradeListIntent.LoadTrades)
        orderViewModel.handleIntent(OrderIntent.LoadOrders)
    }

    GlassPullToRefreshBox(
        isRefreshing = tradeState.isRefreshing,
        onRefresh = { 
            tradeViewModel.refreshData()
            orderViewModel.handleIntent(OrderIntent.LoadOrders)
            marketTrendsViewModel.refreshData()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Market Pairs Section
                item {
                    SectionHeader("بازارها", Icons.AutoMirrored.Filled.KeyboardArrowRight)
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        trends?.let { list ->
                            items(list) { trend ->
                                MarketPairCard(
                                    symbol = trend.asset?.symbol ?: "",
                                    price = trend.price ?: 0.0,
                                    change = 2.45, // Mock change
                                    onClick = { navigation.navigate("chart/${trend.asset?.id}") }
                                )
                            }
                        }
                    }
                }

                // Open Orders Section
                item {
                    SectionHeader("سفارشات باز", painterResource(R.drawable.trade_s))
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val openOrders = orderState.orders
                    if (openOrders.isEmpty()) {
                        EmptyStateCard("سفارش بازی ندارید")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            openOrders.forEachIndexed { index, order ->
                                val animState = remember { MutableTransitionState(false).apply { targetState = true } }
                                AnimatedVisibility(
                                    visibleState = animState,
                                    enter = fadeIn(animationSpec = tween(400, delayMillis = index * 100)) + 
                                            expandVertically(animationSpec = tween(400, delayMillis = index * 100))
                                ) {
                                    OrderCard(order)
                                }
                            }
                        }
                    }
                }

                // Recent Trades Section
                item {
                    SectionHeader("تاریخچه معاملات اخیر", Icons.Default.Info)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val trades = tradeState.trades
                    if (trades.isEmpty()) {
                        EmptyStateCard("معامله‌ای ثبت نشده است")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            trades.take(5).forEachIndexed { index, trade ->
                                val animState = remember { MutableTransitionState(false).apply { targetState = true } }
                                AnimatedVisibility(
                                    visibleState = animState,
                                    enter = fadeIn(animationSpec = tween(400, delayMillis = (index + 3) * 100))
                                ) {
                                    RecentTradeCard(trade)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: Any) {
    val colorScheme = MaterialTheme.colorScheme
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = title,
            color = colorScheme.onSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(12.dp))
        when (icon) {
            is androidx.compose.ui.graphics.vector.ImageVector -> Icon(imageVector = icon, contentDescription = null, tint = colorScheme.primary, modifier = Modifier.size(24.dp))
            is androidx.compose.ui.graphics.painter.Painter -> Icon(painter = icon, contentDescription = null, tint = colorScheme.primary, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun MarketPairCard(symbol: String, price: Double, change: Double, onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    
    GlassCard(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        opacity = 0.15f
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "$symbol/USDT", color = colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "$${String.format("%.2f", price)}", color = colorScheme.onSurface, fontSize = 18.sp, fontWeight = FontWeight.Black)
            Text(
                text = "${if (change > 0) "+" else ""}$change%",
                color = if (change > 0) colorScheme.tertiary else colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Mock Sparkline
            Sparkline(color = if (change > 0) colorScheme.tertiary else colorScheme.primary)
        }
    }
}

@Composable
fun Sparkline(color: Color) {
    Canvas(modifier = Modifier.fillMaxWidth().height(30.dp)) {
        val path = Path()
        val width = size.width
        val height = size.height
        
        path.moveTo(0f, height * 0.7f)
        path.quadraticTo(width * 0.2f, height * 0.2f, width * 0.4f, height * 0.5f)
        path.quadraticTo(width * 0.6f, height * 0.8f, width * 0.8f, height * 0.3f)
        path.lineTo(width, height * 0.4f)
        
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
fun OrderCard(order: com.example.tradeapp.models.Order) {
    val colorScheme = MaterialTheme.colorScheme
    
    GlassCard(opacity = 0.12f) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (order.orderType == OrderType.BUY) colorScheme.tertiary else colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (order.orderType == OrderType.BUY) "خرید" else "فروش",
                        color = if (order.orderType == OrderType.BUY) colorScheme.tertiary else colorScheme.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = order.asset?.symbol ?: "", color = colorScheme.onSurface, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = "مقدار: ${order.quantity}", color = colorScheme.onSurface.copy(alpha = 0.8f), fontSize = 14.sp)
                Text(text = "قیمت: $${order.price}", color = colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RecentTradeCard(trade: com.example.tradeapp.models.Trade) {
    val colorScheme = MaterialTheme.colorScheme
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = trade.asset?.symbol ?: "", color = colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${trade.quantity}",
                color = colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "$${trade.price}", color = colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}

@Composable
fun EmptyStateCard(text: String) {
    val colorScheme = MaterialTheme.colorScheme
    
    GlassCard(opacity = 0.05f) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = colorScheme.onSurface.copy(alpha = 0.4f), fontSize = 14.sp)
        }
    }
}
