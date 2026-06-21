package com.example.tradeapp.ui.pages

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.tradeapp.ui.tools.OptionButton
import com.example.tradeapp.ui.tools.StateBoxCard
import com.example.tradeapp.ui.tools.StateCard
import com.example.tradeapp.ui.tools.TimeDropdownMenu
import com.example.tradeapp.utils.NamePage
import com.example.tradeapp.viewmodel.OrderViewModel
import com.example.tradeapp.viewmodel.TradeListViewModel
import com.example.tradeapp.viewmodel.intent.OrderIntent
import com.example.tradeapp.viewmodel.intent.TradeListIntent
import com.example.tradeapp.viewmodel.state.OrderType

@Composable
fun TradePage(
    navigation: NavHostController,
    tradeViewModel: TradeListViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
) {
    val tradeState by tradeViewModel.state.collectAsState()
    val orderState by orderViewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        tradeViewModel.handleIntent(TradeListIntent.LoadTrades)
        orderViewModel.handleIntent(OrderIntent.LoadOrders)
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        LazyRow {
            items(10) {
                OptionButton()
                Spacer(Modifier.width(10.dp))
            }
        }
        Spacer(Modifier.height(20.dp))

        LazyRow {
            items(10) {
                StateCard()
                Spacer(Modifier.width(10.dp))
            }
        }
        Spacer(Modifier.height(24.dp))

        // Open Sell Orders
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TimeDropdownMenu()
            Text(
                text = "فروش های باز",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(12.dp))
        orderState.orders.filter { it.orderType == OrderType.SELL }.forEach {
            StateBoxCard(nameCoin = it.asset?.name ?: "", symbol = it.asset?.symbol ?: "", count = it.quantity.toString(), price = it.price.toString()) {
                navigation.navigate("${NamePage.CHART}/${it.id}")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(Modifier.height(32.dp))

        // Open Buy Orders
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TimeDropdownMenu()
            Text(
                text = "خرید های باز",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(12.dp))
        orderState.orders.filter { it.orderType == OrderType.BUY }.forEach {
            StateBoxCard(nameCoin = it.asset?.name ?: "", symbol = it.asset?.symbol ?: "", count = it.quantity.toString(), price = it.price.toString()) {
                navigation.navigate("${NamePage.CHART}/${it.id}")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(Modifier.height(32.dp))

        // Closed Trades
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TimeDropdownMenu()
            Text(
                text = "ترید های بسته در لحظه",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(12.dp))
        tradeState.trades.forEach {
            StateBoxCard(nameCoin = it.asset?.name ?: "", symbol = it.asset?.symbol ?: "", count = it.quantity.toString(), price = it.price.toString()) {
                navigation.navigate("${NamePage.CHART}/${it.asset?.id}")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(100.dp)) // Extra space for FAB and BottomBar
    }
}
