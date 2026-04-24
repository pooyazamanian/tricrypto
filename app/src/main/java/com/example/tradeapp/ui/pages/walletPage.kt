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
import com.example.tradeapp.damin.model.UserAsset
import com.example.tradeapp.ui.model.UiUserAsset
import com.example.tradeapp.ui.tools.StateBoxCard
import com.example.tradeapp.viewmodel.AssetViewModel
import com.example.tradeapp.viewmodel.UserAssetViewModel
import com.example.tradeapp.viewmodel.intent.AssetIntent
import com.example.tradeapp.viewmodel.intent.UserAssetIntent
import kotlin.String
import kotlin.collections.emptyList

@Composable
fun WalletPage(
    navigation: NavHostController,
    userAssetViewModel: UserAssetViewModel = hiltViewModel(),
    assetViewModel: AssetViewModel = hiltViewModel()

) {
    val userAssetState by userAssetViewModel.state.collectAsState()
    val assetState by assetViewModel.state.collectAsState()
    val listAsset = remember { mutableStateListOf<UiUserAsset>() }
    LaunchedEffect(userAssetState.userAssets, assetState.assets) {
        if (userAssetState.userAssets.isEmpty() && assetState.assets.isEmpty()) return@LaunchedEffect
        userAssetState.userAssets.forEach { userAsset ->
            val selectedAsset = assetState.assets.find { assets ->
                assets.id == userAsset.assetId
            }
            selectedAsset?.let {
                listAsset.add(
                    UiUserAsset(
                        quantity = userAsset.quantity,
                        name = it.name,
                        symbol = it.symbol,
                        isActive = it.isActive
                    )
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        assetViewModel.handleIntent(AssetIntent.LoadAssets)
        userAssetViewModel.handleIntent(UserAssetIntent.LoadUserAssets)
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "دارایی ها:",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(10.dp))
            listAsset.forEach {
                StateBoxCard(
                    nameCoin = it.name ?: "",
                    count = it.quantity.toString(),
                    percent = it.percentage
                )

            }

        }
    }

}