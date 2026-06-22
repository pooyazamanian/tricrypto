package com.example.tradeapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import com.example.tradeapp.R
import androidx.compose.runtime.*
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
import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.models.Order
import com.example.tradeapp.dto.UserAsset
import com.example.tradeapp.viewmodel.AssetViewModel
import com.example.tradeapp.viewmodel.CreateOrderViewModel
import com.example.tradeapp.viewmodel.UserAssetViewModel
import com.example.tradeapp.viewmodel.effect.CreatingOrderEffect
import com.example.tradeapp.viewmodel.intent.OrderingIntent
import com.example.tradeapp.viewmodel.state.OrderType
import kotlinx.coroutines.launch
import com.example.tradeapp.ui.theme.glassColors

@Composable
fun NetWorkConnectionDialog(onDismissRequest: () -> Unit = {}) {
    val colorScheme = MaterialTheme.colorScheme
    
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
                        colorScheme.surface,
                        shape = RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp)
                    )
                    .clip(RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "اتصال به اینترنت",
                    color = colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, bottom = 10.dp),
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
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
                    color = colorScheme.onSurface.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

enum class TradeStep {
    SELECT_TYPE,
    SELECT_ASSET,
    ENTER_DETAILS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeCryptoBottomSheet(
    isSheetVisible: SheetState,
    assetViewModel: AssetViewModel = hiltViewModel(),
    userAssetViewModel: UserAssetViewModel = hiltViewModel(),
    createOrderViewModel: CreateOrderViewModel = hiltViewModel()
) {
    val assetState by assetViewModel.state.collectAsState()
    val userAssetState by userAssetViewModel.state.collectAsState()
    val tradingState by createOrderViewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val colorScheme = MaterialTheme.colorScheme

    var currentStep by remember { mutableStateOf(TradeStep.SELECT_TYPE) }
    var tradeAmount by remember { mutableStateOf(TextFieldValue("")) }
    var tradePrice by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        createOrderViewModel.effect.collect { effect ->
            when (effect) {
                is CreatingOrderEffect.ShowSuccess -> {
                    errorMessage = null
                    scope.launch { isSheetVisible.hide() }
                }
                is CreatingOrderEffect.ShowError -> {
                    errorMessage = effect.message
                }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { 
            scope.launch { isSheetVisible.hide() }
            currentStep = TradeStep.SELECT_TYPE // Reset for next time
        },
        sheetState = isSheetVisible,
        containerColor = colorScheme.surface,
        scrimColor = Color.Black.copy(alpha = 0.6f),
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = colorScheme.onSurface.copy(alpha = 0.3f))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "خرید و فروش ارز",
                color = colorScheme.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            when (currentStep) {
                TradeStep.SELECT_TYPE -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TradeTypeOption(
                            text = "خرید",
                            color = colorScheme.tertiary,
                            icon = R.drawable.cardreceive,
                            onClick = {
                                createOrderViewModel.handleIntent(OrderingIntent.SetTradeType(OrderType.BUY))
                                currentStep = TradeStep.SELECT_ASSET
                            }
                        )
                        TradeTypeOption(
                            text = "فروش",
                            color = colorScheme.primary,
                            icon = R.drawable.cardsend,
                            onClick = {
                                createOrderViewModel.handleIntent(OrderingIntent.SetTradeType(OrderType.SELL))
                                currentStep = TradeStep.SELECT_ASSET
                            }
                        )
                    }
                }

                TradeStep.SELECT_ASSET -> {
                    Text(
                        text = "انتخاب ارز:",
                        color = colorScheme.onSurface.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.End).padding(bottom = 16.dp)
                    )
                    
                    if (assetState.isLoading) {
                        CircularProgressIndicator(color = colorScheme.primary)
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.heightIn(max = 300.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(assetState.assets) { asset ->
                                CryptoSelectionCard(
                                    asset = asset,
                                    isSelected = tradingState.selectedAsset?.id == asset.id,
                                    onClick = { 
                                        createOrderViewModel.handleIntent(OrderingIntent.SelectAsset(asset))
                                        currentStep = TradeStep.ENTER_DETAILS
                                    }
                                )
                            }
                        }
                    }
                    
                    TextButton(
                        onClick = { currentStep = TradeStep.SELECT_TYPE },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("بازگشت", color = colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }

                TradeStep.ENTER_DETAILS -> {
                    val asset = tradingState.selectedAsset
                    if (asset != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = asset.name ?: "",
                                    color = colorScheme.onSurface,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = asset.symbol ?: "",
                                    color = colorScheme.onSurface.copy(alpha = 0.6f),
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            AsyncImage(
                                model = asset.logoUrl,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp).clip(CircleShape),
                                placeholder = painterResource(R.drawable.test)
                            )
                        }

                        GlassTextField(
                            value = tradeAmount.text,
                            onValueChange = { tradeAmount = TextFieldValue(it) },
                            label = "مقدار (${asset.symbol ?: ""})",
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        GlassTextField(
                            value = tradePrice.text,
                            onValueChange = { tradePrice = TextFieldValue(it) },
                            label = "قیمت پیشنهادی (دلار)",
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                color = colorScheme.error,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        GlassButton(
                            text = if (tradingState.orderType == OrderType.BUY) "تایید خرید" else "تایید فروش",
                            onClick = {
                                val amount = tradeAmount.text.toDoubleOrNull() ?: 0.0
                                val price = tradePrice.text.toDoubleOrNull() ?: 0.0
                                if (amount > 0 && price > 0) {
                                    val order = Order(
                                        orderType = tradingState.orderType,
                                        quantity = amount,
                                        price = price,
                                    )
                                    createOrderViewModel.handleIntent(OrderingIntent.CreateOrder(order))
                                } else {
                                    errorMessage = "لطفا مقادیر معتبر وارد کنید"
                                }
                            },
                            isLoading = tradingState.isLoading,
                            containerColor = if (tradingState.orderType == OrderType.BUY) colorScheme.tertiary else colorScheme.primary
                        )
                        
                        TextButton(
                            onClick = { currentStep = TradeStep.SELECT_ASSET },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("تغییر ارز", color = colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.TradeTypeOption(
    text: String,
    color: Color,
    icon: Int,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(icon),
                contentDescription = text,
                tint = color,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = text,
                color = colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CryptoSelectionCard(
    asset: AssetDto,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) colorScheme.primary.copy(alpha = 0.2f) else colorScheme.onSurface.copy(alpha = 0.05f))
            .border(
                1.dp,
                if (isSelected) colorScheme.primary else colorScheme.onSurface.copy(alpha = 0.1f),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = asset.logoUrl,
                contentDescription = null,
                modifier = Modifier.size(32.dp).clip(CircleShape),
                placeholder = painterResource(R.drawable.test)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = asset.symbol ?: "",
                color = colorScheme.onSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
