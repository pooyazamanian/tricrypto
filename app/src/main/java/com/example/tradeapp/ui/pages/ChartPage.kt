package com.example.tradeapp.ui.pages

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import coil3.compose.AsyncImage
import com.example.tradeapp.R
import com.example.tradeapp.damin.model.Asset
import com.example.tradeapp.ui.tools.MainCard
import com.example.tradeapp.ui.tools.RowContent
import com.example.tradeapp.ui.tools.charts.CandlestickChartView
import com.example.tradeapp.viewmodel.ChartEffect
import com.example.tradeapp.viewmodel.ChartViewModel
import com.example.tradeapp.viewmodel.TimeRange
import com.example.tradeapp.viewmodel.intent.ChartIntent
import com.example.tradeapp.viewmodel.state.ChartState
import com.example.tradeapp.viewmodel.state.KeyStats
import com.example.tradeapp.viewmodel.state.PriceInfo
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.candlestickSeries
import java.text.DecimalFormat

@Composable
fun ChartPage(
    navigation: NavHostController,
    assetId: String,
    viewModel: ChartViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val modelProducer = remember { CartesianChartModelProducer() }

    // ✅ این خط رو اضافه کن - selectedTimeRange
    var selectedTimeRange by remember { mutableStateOf(TimeRange.ONE_DAY) }

    // بارگذاری اولیه Asset
    LaunchedEffect(assetId) {
        viewModel.handleIntent(ChartIntent.LoadAssetInfo(assetId))
    }

    // ✅ به‌روزرسانی نمودار
    LaunchedEffect(state.historyData, selectedTimeRange) {
        val data = state.historyData

        if (data != null && data.s == "ok") {
            try {
                val currentTime = System.currentTimeMillis() / 1000 // به ثانیه
                val minTime = when (selectedTimeRange) {
                    TimeRange.ONE_DAY_BEFORE -> currentTime - (48 * 60 * 60)
                    TimeRange.ONE_DAY -> currentTime - (24 * 60 * 60)
                    TimeRange.FIVE_DAYS -> currentTime - (5 * 24 * 60 * 60)
                    TimeRange.ONE_MONTH -> currentTime - (30 * 24 * 60 * 60)
                    TimeRange.FIVE_MONTHS -> currentTime - (150 * 24 * 60 * 60)
                    TimeRange.ONE_YEAR -> currentTime - (365 * 24 * 60 * 60)
                    TimeRange.ALL -> Long.MIN_VALUE
                }

                // فیلتر کردن داده‌ها
                val filteredIndices = data.t.mapIndexedNotNull { index, time ->
                    if (time >= minTime && time <= currentTime) index else null
                }

                Log.d("ChartPage", "Filtered: ${filteredIndices.size} out of ${data.t.size}")

                if (filteredIndices.isNotEmpty()) {
                    // ✅ محدود کردن به 500 تا - برای جلوگیری از OutOfMemory
                    val limitedIndices = filteredIndices.takeLast(500)

                    modelProducer.runTransaction {
                        candlestickSeries(
                            x = limitedIndices.map { data.t[it].toDouble() },
                            opening = limitedIndices.map { data.o[it] },
                            closing = limitedIndices.map { data.c[it] },
                            low = limitedIndices.map { data.l[it] },
                            high = limitedIndices.map { data.h[it] }
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("ChartPage", "Chart error", e)
                Toast.makeText(context, "خطا در نمایش نمودار", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Auto-refresh
    DisposableEffect(Unit) {
        viewModel.startAutoRefresh()
        onDispose {
            viewModel.stopAutoRefresh()
        }
    }

    ChartPageContent(
        state = state,
        modelProducer = modelProducer,
        selectedTimeRange = selectedTimeRange, // ✅ پاس بده
        onTimeRangeSelected = { timeRange ->
            selectedTimeRange = timeRange // ✅ تغییر بده
            viewModel.handleIntent(ChartIntent.SelectTimeRange(timeRange))
        },
        onRefresh = {
            viewModel.handleIntent(ChartIntent.RefreshData)
        },
        onBuyClick = {
            // Navigate
        },
        onSellClick = {
            // Navigate
        },
        onBackClick = { navigation.popBackStack() }
    )
}
@Composable
private fun ChartPageContent(
    state: ChartState,
    modelProducer: CartesianChartModelProducer,
    selectedTimeRange: TimeRange, // ✅ اضافه شد
    onTimeRangeSelected: (TimeRange) -> Unit,
    onRefresh: () -> Unit,
    onBuyClick: () -> Unit,
    onSellClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        // هدر
        AssetHeader(
            asset = state.asset,
            isLoading = state.isLoading,
            isRefreshing = state.isRefreshing,
            onBackClick = onBackClick,
            onRefreshClick = onRefresh
        )

        Spacer(Modifier.height(10.dp))

        // قیمت
        CurrentPriceSection(priceInfo = state.priceInfo)

        Spacer(Modifier.height(10.dp))

        // تغییرات
        PriceChangeSection(priceInfo = state.priceInfo)

        Spacer(Modifier.height(10.dp))

        // نمودار
        if (state.isLoading && state.historyData == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            CandlestickChartView(
                modelProducer = modelProducer,
                selectedTimeRange = selectedTimeRange,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(10.dp))

        // دکمه‌های بازه زمانی
        TimeRangeButtons(
            selectedTimeRange = selectedTimeRange,
            onTimeRangeSelected = onTimeRangeSelected
        )

        Spacer(Modifier.height(10.dp))

        // دکمه‌های خرید و فروش
        BuySellButtons(
            onBuyClick = onBuyClick,
            onSellClick = onSellClick,
            enabled = !state.isLoading
        )

        Spacer(Modifier.height(15.dp))

        // آمار
        KeyStatsSection(keyStats = state.keyStats)
    }
}

@Composable
private fun TimeRangeButtons(
    selectedTimeRange: TimeRange,
    onTimeRangeSelected: (TimeRange) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(TimeRange.values()) { timeRange ->
            FilterChip(
                selected = selectedTimeRange == timeRange,
                onClick = { onTimeRangeSelected(timeRange) },
                label = { Text(timeRange.label, fontSize = 12.sp) },
            )
        }
    }
}

@Composable
private fun TimeRangeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(text, fontSize = 10.sp)
    }
}
@Composable
private fun AssetHeader(
    asset: Asset?,
    isLoading: Boolean,
    isRefreshing: Boolean,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // لوگوی Asset
            if (asset?.logoUrl != null) {
                AsyncImage(
                    model = asset.logoUrl,
                    contentDescription = asset.name,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.test),
                    error = painterResource(R.drawable.test)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.test),
                    contentDescription = "asset",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.FillBounds
                )
            }

            Spacer(Modifier.width(10.dp))

            Column {
                Text(
                    text = asset?.symbol ?: "...",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = asset?.name ?: "در حال بارگذاری...",
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onRefreshClick,
                enabled = !isLoading
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = Color.White,
                    modifier = if (isRefreshing) {
                        Modifier.graphicsLayer() { rotationZ = 360f }
                    } else {
                        Modifier
                    }
                )
            }
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    }
}



@Composable
private fun CurrentPriceSection(priceInfo: PriceInfo?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = priceInfo?.currentPrice?.let {
                DecimalFormat("#,###.##").format(it)
            } ?: "...",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = "تومان",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun PriceChangeSection(priceInfo: PriceInfo?) {
    Row(
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val priceChangePercent = priceInfo?.priceChangePercent ?: 0.0
        val priceChange = priceInfo?.priceChange ?: 0.0
        val isPositive = priceChangePercent >= 0
        val color = if (isPositive) {
            MaterialTheme.colorScheme.surfaceTint
        } else {
            MaterialTheme.colorScheme.error
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(color.copy(alpha = 0.3f))
                .padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                tint = color,
                modifier = Modifier.rotate(if (isPositive) 90f else -90f),
                contentDescription = ""
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "${if (priceChangePercent >= 0) "+" else ""}%.2f%%".format(priceChangePercent),
                color = color,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(Modifier.width(10.dp))

        Text(
            text = "${if (priceChange >= 0) "+" else ""}${DecimalFormat("#,###.##").format(priceChange)}",
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun TimeRangeButtons(
    selectedTimeRange: TimeRange,
    onTimeRangeSelected: (TimeRange) -> Unit,
    enabled: Boolean
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(TimeRange.values()) { timeRange ->
            FilterChip(
                selected = selectedTimeRange == timeRange,
                onClick = { onTimeRangeSelected(timeRange) },
                label = { Text(timeRange.label, fontSize = 12.sp) },
                enabled = enabled
            )
        }
    }
}

@Composable
private fun ChartSection(
    state: ChartState,
    modelProducer: CartesianChartModelProducer,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading && state.historyData == null -> {
                CircularProgressIndicator()
            }
            state.error != null && state.historyData == null -> {
                ErrorView(
                    error = state.error,
                    onRetry = onRefresh
                )
            }
            else -> {
                CandlestickChartView(
                    modelProducer = modelProducer,
                    selectedTimeRange = state.selectedTimeRange,
                    modifier = Modifier.fillMaxSize()
                )

                if (state.isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(20.dp)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.HONEYCOMB_MR2)
@Composable
private fun BuySellButtons(
    onBuyClick: () -> Unit,
    onSellClick: () -> Unit,
    enabled: Boolean
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp - 50.dp

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onSellClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
            ),
            modifier = Modifier
                .width(screenWidthDp / 2)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.error,
                    RoundedCornerShape(10.dp)
                )
        ) {
            Text("فروش", color = Color.White)
        }
        Spacer(Modifier.width(5.dp))
        Button(
            onClick = onBuyClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
            ),
            modifier = Modifier.width(screenWidthDp / 2)
        ) {
            Text("خرید", color = Color.White)
        }
    }
}

@Composable
private fun KeyStatsSection(keyStats: KeyStats?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = ":آمار کلیدی",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(Modifier.height(5.dp))

        MainCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                keyStats?.let { stats ->
                    RowContent("قیمت باز شدن", "${DecimalFormat("#,###.##").format(stats.openPrice)} تومان")
                    Divider()
                    RowContent("قیمت بسته شدن", "${DecimalFormat("#,###.##").format(stats.closePrice)} تومان")
                    Divider()
                    RowContent("بالاترین قیمت", "${DecimalFormat("#,###.##").format(stats.highPrice)} تومان")
                    Divider()
                    RowContent("پایین‌ترین قیمت", "${DecimalFormat("#,###.##").format(stats.lowPrice)} تومان")
                    Divider()
                    RowContent("قیمت میانگین", "${DecimalFormat("#,###.##").format(stats.avgPrice)} تومان")
                    Divider()
                    RowContent("حجم کل معاملات", DecimalFormat("#,###").format(stats.volume))
                    Divider()
                    RowContent("تعداد معاملات", stats.totalTrades.toString())
                } ?: run {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun Divider() {
    Spacer(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.primary)
    )
}

@Composable
private fun RowContent(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = value,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 13.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ErrorView(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
            )
        ) {
            Text("تلاش مجدد")
        }
    }
}
