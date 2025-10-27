package com.example.tradeapp.ui.tools

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import com.example.tradeapp.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.tradeapp.damin.model.Asset
import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.utils.animatedShimmer
import com.example.tradeapp.viewmodel.TradeViewModel
import com.example.tradeapp.viewmodel.UiState
import com.example.tradeapp.viewmodel.intent.TradeEffect
import com.example.tradeapp.viewmodel.intent.TradeIntent
import com.example.tradeapp.viewmodel.intent.TradeType
import kotlinx.coroutines.launch

@Composable
fun NetWorkConnectionDialog( onDismissRequest: () -> Unit = {}) {
    Dialog(onDismissRequest = {
        onDismissRequest()
    }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background (Color.Transparent),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp)
                    )
                    .clip(RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    text = "اتصال به اینترنت",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, bottom = 10.dp),
                    textAlign = TextAlign .Right,
                    fontWeight = FontWeight(700),
                    fontSize = 32.sp
                )
                Image(
                    painter = painterResource(id = R.drawable.notconnected),
                    contentDescription = "no internet",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "به اینترنت متصل نیستید. برای استفاده از اپلیکیشن به اینترنت متصل شوید",
                    fontWeight = FontWeight(400),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

}





@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeCryptoBottomSheet(
    isSheetVisible: SheetState,
    viewModel: TradeViewModel
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    var tradeAmount by remember { mutableStateOf(TextFieldValue("")) }
    var tradePrice by remember { mutableStateOf(TextFieldValue("")) }
    var selectedAsset by remember { mutableStateOf<Asset?>(null) }
    var isBuyMode by remember { mutableStateOf(true) }
    var useMarketPrice by remember { mutableStateOf(true) } // true = قیمت لحظه‌ای، false = قیمت دستی

    // قیمت‌های پیشنهادی
    val suggestedAmounts = remember {
        if (isBuyMode) {
            listOf("0.01", "0.05", "0.1", "0.5", "1")
        } else {
            listOf("25%", "50%", "75%", "100%")
        }
    }
    var selectedSuggestedAmount by remember { mutableStateOf<String?>(null) }

    // Collect effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TradeEffect.ShowError -> {
                    // نمایش پیام خطا
                }
                is TradeEffect.ShowSuccess -> {
                    // نمایش پیام موفقیت
                    scope.launch { isSheetVisible.hide() }
                }
                else -> {}
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.wrapContentHeight(),
        sheetState = isSheetVisible,
        onDismissRequest = {
            scope.launch { isSheetVisible.hide() }
        },
        dragHandle = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            scope.launch { isSheetVisible.hide() }
                        }
                )

                Text(
                    text = "معامله رمزارز",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // سوییچ خرید/فروش
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // دکمه خرید
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                if (isBuyMode) MaterialTheme.colorScheme.primary
                                else Color.Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                isBuyMode = true
                                selectedSuggestedAmount = null
                                tradeAmount = TextFieldValue("")
                                tradePrice = TextFieldValue("")
                                viewModel.handleIntent(TradeIntent.SetTradeType(TradeType.BUY))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "خرید",
                            color = if (isBuyMode) Color.White
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // دکمه فروش
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                if (!isBuyMode) MaterialTheme.colorScheme.error
                                else Color.Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                isBuyMode = false
                                selectedSuggestedAmount = null
                                tradeAmount = TextFieldValue("")
                                tradePrice = TextFieldValue("")
                                viewModel.handleIntent(TradeIntent.SetTradeType(TradeType.SELL))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "فروش",
                            color = if (!isBuyMode) Color.White
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // عنوان انتخاب رمزارز
                Text(
                    text = if (isBuyMode) "رمزارز مورد نظر برای خرید:"
                    else "رمزارز مورد نظر برای فروش:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                // لیست رمزارزها
                if (state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // در حالت فروش فقط رمزارزهایی که کاربر دارد نمایش داده می‌شود
                        val displayAssets = if (!isBuyMode) {
                            state.assets.filter { asset ->
                                state.userAssets.any { userAsset ->
                                    userAsset.assetId == asset.id && (userAsset.quantity ?: 0.0) > 0
                                }
                            }
                        } else {
                            state.assets
                        }

                        items(displayAssets) { asset ->
                            CryptoAssetCard(
                                asset = asset,
                                isSelected = selectedAsset?.id == asset.id,
                                userQuantity = if (!isBuyMode) {
                                    state.userAssets.find { it.assetId == asset.id }?.quantity ?: 0.0
                                } else null,
                                onClick = {
                                    selectedAsset = asset
                                    // اگر قیمت لحظه‌ای انتخاب شده، قیمت رو آپدیت کن
                                    if (useMarketPrice) {
                                        tradePrice = TextFieldValue(state.currentPrice?.toString() ?: "")
                                    }
                                    viewModel.handleIntent(TradeIntent.SelectAsset(asset))
                                }
                            )
                        }
                    }
                }

                // باکس نمایش قیمت لحظه‌ای و تغییرات
                selectedAsset?.let { asset ->
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "قیمت لحظه‌ای ${asset.symbol}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "$${state.currentPrice ?: "0.00"}",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            // باکس تغییرات قیمت
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if ((state.priceChange ?: 0.0) >= 0)
                                        Color(0xFF4CAF50).copy(alpha = 0.1f)
                                    else
                                        Color(0xFFF44336).copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if ((state.priceChange ?: 0.0) >= 0)
                                            Icons.Default.KeyboardArrowUp
                                        else
                                            Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if ((state.priceChange ?: 0.0) >= 0)
                                            Color(0xFF4CAF50)
                                        else
                                            Color(0xFFF44336)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${state.priceChangePercent ?: "0.0"}%",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if ((state.priceChange ?: 0.0) >= 0)
                                            Color(0xFF4CAF50)
                                        else
                                            Color(0xFFF44336)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // بخش انتخاب نوع قیمت (لحظه‌ای یا دستی)
                Text(
                    text = "نوع قیمت:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // دکمه قیمت لحظه‌ای
                    FilterChip(
                        selected = useMarketPrice,
                        onClick = {
                            useMarketPrice = true
                            // قیمت رو به قیمت لحظه‌ای تغییر بده
                            tradePrice = TextFieldValue(state.currentPrice?.toString() ?: "")
                        },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("قیمت لحظه‌ای")
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )

                    // دکمه قیمت دستی
                    FilterChip(
                        selected = !useMarketPrice,
                        onClick = {
                            useMarketPrice = false
                            tradePrice = TextFieldValue("")
                        },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Build,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("قیمت دستی")
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ورودی قیمت
                OutlinedTextField(
                    value = tradePrice,
                    onValueChange = {
                        if (!useMarketPrice) {
                            tradePrice = it
                        }
                    },
                    label = {
                        Text(
                            if (useMarketPrice) "قیمت لحظه‌ای" else "قیمت پیشنهادی شما"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !useMarketPrice, // فقط در حالت دستی فعال باشد
                    trailingIcon = {
                        Row(
                            modifier = Modifier.padding(end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "دلار",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (!useMarketPrice) {
                                Spacer(modifier = Modifier.width(8.dp))
                                // دکمه پاک کردن
                                IconButton(
                                    onClick = { tradePrice = TextFieldValue("") },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        disabledTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                // نمایش هشدار در صورت اختلاف قیمت
                if (!useMarketPrice && tradePrice.text.isNotEmpty() && selectedAsset != null) {
                    val customPrice = tradePrice.text.toDoubleOrNull() ?: 0.0
                    val marketPrice = state.currentPrice ?: 0.0
                    val priceDifference = ((customPrice - marketPrice) / marketPrice * 100)

                    if (kotlin.math.abs(priceDifference) > 5) { // اگر بیش از 5% اختلاف داشت
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF3E0)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFFFF9800),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (priceDifference > 0)
                                        "قیمت شما ${kotlin.math.abs(priceDifference).toInt()}% بالاتر از نرخ بازار است"
                                    else
                                        "قیمت شما ${kotlin.math.abs(priceDifference).toInt()}% پایین‌تر از نرخ بازار است",
                                    fontSize = 12.sp,
                                    color = Color(0xFFE65100)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // مقادیر پیشنهادی
                Text(
                    text = if (isBuyMode) "مقادیر پیشنهادی:" else "درصد فروش:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(suggestedAmounts) { amount ->
                        SuggestedAmountChip(
                            amount = amount,
                            isSelected = selectedSuggestedAmount == amount,
                            onClick = {
                                selectedSuggestedAmount = amount

                                // محاسبه مقدار بر اساس انتخاب
                                if (!isBuyMode && selectedAsset != null) {
                                    val userAsset = state.userAssets.find { it.assetId == selectedAsset?.id }
                                    userAsset?.quantity?.let { quantity ->
                                        val percentage = amount.removeSuffix("%").toDoubleOrNull() ?: 0.0
                                        val calculatedAmount = (quantity * percentage) / 100
                                        tradeAmount = TextFieldValue(calculatedAmount.toString())
                                    }
                                } else if (isBuyMode) {
                                    tradeAmount = TextFieldValue(amount)
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ورودی مقدار
                Text(
                    text = if (isBuyMode) "مقدار خرید:" else "مقدار فروش:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = tradeAmount,
                    onValueChange = {
                        tradeAmount = it
                        selectedSuggestedAmount = null // پاک کردن انتخاب هنگام تایپ دستی
                    },
                    label = { Text("مقدار") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Text(
                            text = selectedAsset?.symbol ?: "واحد",
                            modifier = Modifier.padding(end = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    singleLine = true
                )

                // نمایش موجودی در حالت فروش
                if (!isBuyMode && selectedAsset != null) {
                    val userAsset = state.userAssets.find { it.assetId == selectedAsset?.id }
                    userAsset?.quantity?.let { quantity ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "موجودی شما:",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$quantity ${selectedAsset?.symbol}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // نمایش ارزش تقریبی معامله
                if (tradeAmount.text.isNotEmpty() && tradePrice.text.isNotEmpty() && selectedAsset != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "ارزش کل معامله:",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                val totalValue = (tradeAmount.text.toDoubleOrNull() ?: 0.0) *
                                        (tradePrice.text.toDoubleOrNull() ?: 0.0)
                                Text(
                                    text = "$${"%.2f".format(totalValue)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            if (!useMarketPrice) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "قیمت هر واحد:",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "$${tradePrice.text}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // دکمه تأیید
                Button(
                    onClick = {
                        selectedAsset?.let { asset ->
                            val amount = tradeAmount.text.toDoubleOrNull() ?: 0.0
                            val price = tradePrice.text.toDoubleOrNull() ?: 0.0

                            if (amount > 0 && price > 0) {
                                if (isBuyMode) {
                                    // ایجاد سفارش خرید
                                    val order = Order(
                                        userId = viewModel.userId.toString(),
                                        assetId = asset.id,
                                        orderType = "BUY",
                                        quantity = amount,
                                        price = price,
                                        status = "PENDING"
                                    )
                                    viewModel.handleIntent(TradeIntent.CreateOrder(order))
                                } else {
                                    // بررسی موجودی و ایجاد سفارش فروش
                                    val userAsset = state.userAssets.find { it.assetId == asset.id }
                                    if (userAsset != null && (userAsset.quantity ?: 0.0) >= amount) {
                                        val order = Order(
                                            userId = viewModel.userId.toString(),
                                            assetId = asset.id,
                                            orderType = "SELL",
                                            quantity = amount,
                                            price = price,
                                            status = "PENDING"
                                        )
                                        viewModel.handleIntent(TradeIntent.CreateOrder(order))
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = selectedAsset != null &&
                            tradeAmount.text.isNotEmpty() &&
                            tradePrice.text.isNotEmpty(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBuyMode)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = when {
                                isBuyMode && !useMarketPrice -> "ثبت سفارش خرید"
                                isBuyMode && useMarketPrice -> "خرید فوری"
                                !isBuyMode && !useMarketPrice -> "ثبت سفارش فروش"
                                else -> "فروش فوری"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    )
}

// کامپوننت Chip برای مقادیر پیشنهادی
@Composable
fun SuggestedAmountChip(
    amount: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = amount,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        },
        modifier = Modifier.height(36.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = Color.White
        )
    )
}
// کامپوننت کارت رمزارز
@Composable
fun CryptoAssetCard(
    asset: Asset,
    isSelected: Boolean,
    userQuantity: Double? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // نمایش لوگو
            AsyncImage(
                model = asset.logoUrl,
                contentDescription = asset.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // نام رمزارز
            Text(
                text = asset.symbol ?: "",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = asset.name ?: "",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // نمایش موجودی کاربر
            userQuantity?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "موجودی: $it",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

