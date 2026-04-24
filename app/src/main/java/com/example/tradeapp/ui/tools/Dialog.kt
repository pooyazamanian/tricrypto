package com.example.tradeapp.ui.tools

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.tradeapp.damin.model.UserAsset
import com.example.tradeapp.viewmodel.AssetViewModel
import com.example.tradeapp.viewmodel.OrderViewModel
import com.example.tradeapp.viewmodel.TradeViewModel
import com.example.tradeapp.viewmodel.UserAssetViewModel
import com.example.tradeapp.viewmodel.effect.OrderEffect
import com.example.tradeapp.viewmodel.effect.TradeEffect
import com.example.tradeapp.viewmodel.intent.OrderIntent
import com.example.tradeapp.viewmodel.intent.TradeIntent
import com.example.tradeapp.viewmodel.state.TradeType
import kotlinx.coroutines.launch

@Composable
fun NetWorkConnectionDialog(onDismissRequest: () -> Unit = {}) {
    Dialog(onDismissRequest = {
        onDismissRequest()
    }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
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
                Text(
                    text = "اتصال به اینترنت",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, bottom = 10.dp),
                    textAlign = TextAlign.Right,
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


@Composable
fun SuggestedAmountSection(
    amounts: List<String>,
    selected: String?,
    onAmountSelected: (String) -> Unit
) {
    Text(
        text = if (amounts.first().contains("%")) "درصد فروش:" else "مقادیر پیشنهادی:",
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(amounts) { amount ->
            SuggestedAmountChip(
                amount = amount,
                isSelected = selected == amount,
                onClick = { onAmountSelected(amount) }
            )
        }
    }
}


@SuppressLint("UnrememberedMutableState", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TradeCryptoBottomSheet(
    isSheetVisible: SheetState,
    assetViewModel: AssetViewModel = hiltViewModel(),
    userAssetViewModel: UserAssetViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
    tradeViewModel: TradeViewModel = hiltViewModel(),
) {
    val assetState by assetViewModel.state.collectAsState()
    val userAssetState by userAssetViewModel.state.collectAsState()
    val tradeState by tradeViewModel.state.collectAsState()

    val scope = rememberCoroutineScope()

    var tradeAmount by remember { mutableStateOf(TextFieldValue("")) }
    var tradePrice by remember { mutableStateOf(TextFieldValue("")) }
    var useMarketPrice by remember { mutableStateOf(true) }
    var selectedSuggestedAmount by remember { mutableStateOf<String?>(null) }

    // قیمت لحظه‌ای (برای selected asset)
    val currentPrice = tradeState.selectedAsset?.let { asset ->
        // فرض: در آینده از یک PriceFeed استفاده می‌کنی
        // الان برای تست ثابت داریم
        25000.0 // باید از TradeRepository.getLatestPrice(asset.id) بگیری
    } ?: 0.0

    // تابع کمکی برای ریست کردن inputها
    fun resetInputs() {
        tradeAmount = TextFieldValue("")
        tradePrice = TextFieldValue("")
        selectedSuggestedAmount = null
        if (useMarketPrice) {
            tradePrice = TextFieldValue(currentPrice.toString())
        }
    }
    // Collect effects
    LaunchedEffect(Unit) {
        orderViewModel.effect.collect { effect ->
            when (effect) {
                is OrderEffect.ShowSuccess -> {
                    scope.launch { isSheetVisible.hide() }
                }

                is OrderEffect.ShowError -> {
                    // نمایش خطا در UI (مثلاً Toast)
                }
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.wrapContentHeight(),
        sheetState = isSheetVisible,
        onDismissRequest = { scope.launch { isSheetVisible.hide() } },
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
                        .clickable { scope.launch { isSheetVisible.hide() } }
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
                    .verticalScroll(rememberScrollState())
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
                    TradeModeButton(
                        text = "خرید",
                        isSelected = tradeState.tradeType == TradeType.BUY,
                        color = MaterialTheme.colorScheme.primary,
                        onClick = {
                            tradeViewModel.handleIntent(TradeIntent.SetTradeType(TradeType.BUY))
                            resetInputs()
                        }
                    )
                    TradeModeButton(
                        text = "فروش",
                        isSelected = tradeState.tradeType == TradeType.SELL,
                        color = MaterialTheme.colorScheme.error,
                        onClick = {
                            tradeViewModel.handleIntent(TradeIntent.SetTradeType(TradeType.SELL))
                            resetInputs()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // نمایش لیست دارایی‌ها
                AssetSelectionSection(
                    isBuyMode = tradeState.tradeType == TradeType.BUY,
                    assets = assetState.assets,
                    userAssets = userAssetState.userAssets,
                    selectedAsset = tradeState.selectedAsset,
                    onAssetSelected = { asset ->
                        tradeViewModel.handleIntent(TradeIntent.SelectAsset(asset))
                        if (useMarketPrice) {
                            tradePrice = TextFieldValue(currentPrice.toString())
                        }
                    },
                    isLoading = assetState.isLoading
                )

                // نمایش قیمت لحظه‌ای (ساده‌شده)
                tradeState.selectedAsset?.let { asset ->
                    Spacer(modifier = Modifier.height(16.dp))
                    asset.symbol?.let {
                        CurrentPriceCard(
                            assetSymbol = it,
                            currentPrice = currentPrice
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // نوع قیمت
                PriceTypeSelector(
                    useMarketPrice = useMarketPrice,
                    onToggle = { newValue ->
                        useMarketPrice = newValue
                        if (newValue) {
                            tradePrice = TextFieldValue(currentPrice.toString())
                        } else {
                            tradePrice = TextFieldValue("")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ورودی قیمت
                PriceInputField(
                    tradePrice = tradePrice,
                    onValueChange = { if (!useMarketPrice) tradePrice = it },
                    useMarketPrice = useMarketPrice,
                    currentPrice = currentPrice
                )

                // هشدار اختلاف قیمت (اختیاری)
                PriceDifferenceWarning(
                    useMarketPrice = useMarketPrice,
                    customPriceText = tradePrice.text,
                    marketPrice = currentPrice
                )

                Spacer(modifier = Modifier.height(16.dp))

                // مقادیر پیشنهادی
                val suggestedAmounts = if (tradeState.tradeType == TradeType.BUY) {
                    listOf("0.01", "0.05", "0.1", "0.5", "1")
                } else {
                    listOf("25%", "50%", "75%", "100%")
                }

                SuggestedAmountSection(
                    amounts = suggestedAmounts,
                    selected = selectedSuggestedAmount,
                    onAmountSelected = { amount ->
                        selectedSuggestedAmount = amount
                        if (tradeState.tradeType == TradeType.SELL && tradeState.selectedAsset != null) {
                            val userAsset =
                                userAssetState.userAssets.find { it.assetId == tradeState.selectedAsset!!.id }
                            userAsset?.quantity?.let { qty ->
                                val percent = amount.trim('%').toDoubleOrNull() ?: 0.0
                                val calc = (qty * percent) / 100
                                tradeAmount = TextFieldValue("%.6f".format(calc))
                            }
                        } else {
                            tradeAmount = TextFieldValue(amount)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ورودی مقدار
                AmountInputField(
                    tradeAmount = tradeAmount,
                    onValueChange = {
                        tradeAmount = it
                        selectedSuggestedAmount = null
                    },
                    assetSymbol = tradeState.selectedAsset?.symbol ?: "واحد"
                )

                // نمایش موجودی در حالت فروش
                if (tradeState.tradeType == TradeType.SELL && tradeState.selectedAsset != null) {
                    val userAsset =
                        userAssetState.userAssets.find { it.assetId == tradeState.selectedAsset!!.id }
                    userAsset?.quantity?.let { qty ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "موجودی شما:",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "$qty ${tradeState.selectedAsset!!.symbol}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // ارزش کل معامله
                TotalValuePreview(
                    amountText = tradeAmount.text,
                    priceText = tradePrice.text
                )

                Spacer(modifier = Modifier.height(24.dp))

                // دکمه تأیید
                TradeActionButton(
                    isBuyMode = tradeState.tradeType == TradeType.BUY,
                    useMarketPrice = useMarketPrice,
                    isLoading = orderViewModel.state.value.isLoading,
                    enabled = tradeState.selectedAsset != null &&
                            (tradeAmount.text.toDoubleOrNull() ?: 0.0) > 0 &&
                            (tradePrice.text.toDoubleOrNull() ?: 0.0) > 0,
                    onClick = {
                        tradeState.selectedAsset?.let { asset ->
                            val amount = tradeAmount.text.toDoubleOrNull() ?: 0.0
                            val price = tradePrice.text.toDoubleOrNull() ?: 0.0
                            if (amount > 0 && price > 0) {
                                if (tradeState.tradeType == TradeType.SELL) {
                                    val userAsset =
                                        userAssetState.userAssets.find { it.assetId == asset.id }
                                    if (userAsset == null || (userAsset.quantity
                                            ?: 0.0) < amount
                                    ) {
                                        // نمایش خطا: موجودی کافی نیست
                                        return@TradeActionButton
                                    }
                                }
                                val order = Order(
                                    userId = null ,
                                    assetId = asset.id,
                                    orderType = if (tradeState.tradeType == TradeType.BUY) "BUY" else "SELL",
                                    quantity = amount,
                                    price = price,
                                    status = "PENDING"
                                )
                                orderViewModel.handleIntent(OrderIntent.CreateOrder(order))
                            }
                        }
                    }
                )
            }
        }
    )


}

@Composable
fun TradeActionButton(
    isBuyMode: Boolean,
    useMarketPrice: Boolean,
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isBuyMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
    ) {
        if (isLoading) {
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

@Composable
fun PriceDifferenceWarning(
    useMarketPrice: Boolean,
    customPriceText: String,
    marketPrice: Double
) {
    if (!useMarketPrice && customPriceText.isNotEmpty()) {
        val customPrice = customPriceText.toDoubleOrNull() ?: 0.0
        val priceDifference = if (marketPrice != 0.0) {
            ((customPrice - marketPrice) / marketPrice * 100)
        } else 0.0

        if (kotlin.math.abs(priceDifference) > 5) {
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
                            "قیمت شما ${
                                kotlin.math.abs(priceDifference).toInt()
                            }% بالاتر از نرخ بازار است"
                        else
                            "قیمت شما ${
                                kotlin.math.abs(priceDifference).toInt()
                            }% پایین‌تر از نرخ بازار است",
                        fontSize = 12.sp,
                        color = Color(0xFFE65100)
                    )
                }
            }
        }
    }
}

@Composable
fun TotalValuePreview(
    amountText: String,
    priceText: String
) {
    if (amountText.isNotEmpty() && priceText.isNotEmpty()) {
        val amount = amountText.toDoubleOrNull() ?: 0.0
        val price = priceText.toDoubleOrNull() ?: 0.0
        val totalValue = amount * price

        if (totalValue > 0) {
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
                        Text(
                            text = "$${"%.2f".format(totalValue)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AmountInputField(
    tradeAmount: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    assetSymbol: String
) {
    Text(
        text = "مقدار:",
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = tradeAmount,
        onValueChange = onValueChange,
        label = { Text("مقدار") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            Text(
                text = assetSymbol,
                modifier = Modifier.padding(end = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        singleLine = true
    )
}

@Composable
fun PriceInputField(
    tradePrice: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    useMarketPrice: Boolean,
    currentPrice: Double
) {
    OutlinedTextField(
        value = if (useMarketPrice) TextFieldValue(currentPrice.toString()) else tradePrice,
        onValueChange = {
            if (!useMarketPrice) {
                onValueChange(it)
            }
        },
        label = {
            Text(if (useMarketPrice) "قیمت لحظه‌ای" else "قیمت پیشنهادی شما")
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        enabled = !useMarketPrice,
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
                    IconButton(
                        onClick = { onValueChange(TextFieldValue("")) },
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceTypeSelector(
    useMarketPrice: Boolean,
    onToggle: (Boolean) -> Unit
) {
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
        FilterChip(
            selected = useMarketPrice,
            onClick = { onToggle(true) },
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

        FilterChip(
            selected = !useMarketPrice,
            onClick = { onToggle(false) },
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
}
// 🔹 کامپوننت‌های کوچک (برای خوانایی بیشتر)

@Composable
private fun TradeModeButton(text: String, isSelected: Boolean, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier

            .fillMaxHeight()
            .background(if (isSelected) color else Color.Transparent, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AssetSelectionSection(
    isBuyMode: Boolean,
    assets: List<Asset>,
    userAssets: List<UserAsset>,
    selectedAsset: Asset?,
    onAssetSelected: (Asset) -> Unit,
    isLoading: Boolean
) {
    Text(
        text = if (isBuyMode) "رمزارز مورد نظر برای خرید:" else "رمزارز مورد نظر برای فروش:",
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.height(8.dp))

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val displayAssets = if (isBuyMode) {
            assets
        } else {
            assets.filter { asset ->
                userAssets.any { userAsset ->
                    userAsset.assetId == asset.id && (userAsset.quantity ?: 0.0) > 0
                }
            }
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(displayAssets) { asset ->
                val userQty = if (!isBuyMode) userAssets.find { it.assetId == asset.id }?.quantity
                    ?: 0.0 else null
                CryptoAssetCard(
                    asset = asset,
                    isSelected = selectedAsset?.id == asset.id,
                    userQuantity = userQty,
                    onClick = { onAssetSelected(asset) }
                )
            }
        }
    }
}

@Composable
private fun CurrentPriceCard(assetSymbol: String, currentPrice: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "قیمت لحظه‌ای $assetSymbol",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("$${currentPrice}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            // ساده‌شده — بدون تغییرات درصدی
        }
    }
}

// ... سایر کامپوننت‌ها مثل PriceTypeSelector, PriceInputField, TotalValuePreview, TradeActionButton
// (برای کوتاهی حذف شدن — ولی همین منطق رو دنبال کن)

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

