package com.example.tradeapp.ui.pages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tradeapp.ui.model.UiTrade
import com.example.tradeapp.ui.model.UiUserAsset
import com.example.tradeapp.ui.tools.OptionButton
import com.example.tradeapp.ui.tools.StateBoxCard
import com.example.tradeapp.ui.tools.StateCard
import com.example.tradeapp.ui.tools.TimeDropdownMenu
import com.example.tradeapp.viewmodel.AssetViewModel
import com.example.tradeapp.viewmodel.TradeViewModel
import com.example.tradeapp.viewmodel.intent.AssetIntent
import com.example.tradeapp.viewmodel.intent.TradeIntent

@Composable
fun TradePage(
    navigation: NavHostController,
    tradeViewModel: TradeViewModel = hiltViewModel() ,
    assetViewModel: AssetViewModel = hiltViewModel()
) {

    val tradeState by tradeViewModel.state.collectAsState()
    val assetState by assetViewModel.state.collectAsState()
    val listTrade = remember { mutableStateListOf<UiTrade>() }

    LaunchedEffect(Unit) {
        tradeViewModel.handleIntent(TradeIntent.LoadTrades)
        assetViewModel.handleIntent(AssetIntent.LoadAssets)
    }

    LaunchedEffect(tradeState.trades, assetState.assets) {
        if (tradeState.trades.isEmpty() && assetState.assets.isEmpty()) return@LaunchedEffect
        tradeState.trades.forEach { userAsset ->
            val selectedAsset = assetState.assets.find { assets ->
                assets.id == userAsset.assetId
            }
            selectedAsset?.let {
                listTrade.add(
                    UiTrade(
                        quantity = userAsset.quantity,
                        name = it.name,
                        symbol = it.symbol,
                        isActive = it.isActive
                    )
                )
            }
        }
    }


    Column(
        Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            LazyRow {
                items(10) {
                    OptionButton()
                    Spacer(Modifier.width(10.dp))
                }
            }
            Spacer(Modifier.height(10.dp))

            LazyRow {
                items(10) {
                    StateCard()
                    Spacer(Modifier.width(10.dp))
                }
            }
            Spacer(Modifier.height(25.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimeDropdownMenu()


                Text(
                    text = "فهرست پیگیری های من:",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )


            }
            Spacer(Modifier.height(10.dp))
            listTrade.forEach {
                StateBoxCard(nameCoin =it.name ?: "", count = it.quantity.toString(), price = it.price)
            }

        }

    }
}