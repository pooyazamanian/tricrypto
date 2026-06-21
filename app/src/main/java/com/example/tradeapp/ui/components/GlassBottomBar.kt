package com.example.tradeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradeapp.R
import com.example.tradeapp.utils.NamePage

@Composable
fun GlassBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            .height(84.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(28.dp))
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = R.drawable.home_uns,
                selectedIcon = R.drawable.home_s,
                label = "Home",
                isSelected = currentRoute == NamePage.HOME,
                onClick = { onNavigate(NamePage.HOME) }
            )
            BottomNavItem(
                icon = R.drawable.trade_uns,
                selectedIcon = R.drawable.trade_s,
                label = "Trade",
                isSelected = currentRoute == NamePage.TRADE,
                onClick = { onNavigate(NamePage.TRADE) }
            )
            BottomNavItem(
                icon = R.drawable.wallet,
                selectedIcon = R.drawable.wallet_s,
                label = "Wallet",
                isSelected = currentRoute == NamePage.WALLET,
                onClick = { onNavigate(NamePage.WALLET) }
            )
            BottomNavItem(
                icon = R.drawable.cardreceive, // Using this for Payment as a placeholder if rial icon is not found
                selectedIcon = R.drawable.cardreceive,
                label = "Payment",
                isSelected = currentRoute == NamePage.PAYMENT,
                onClick = { onNavigate(NamePage.PAYMENT) }
            )
            BottomNavItem(
                icon = R.drawable.profile_uns,
                selectedIcon = R.drawable.profile_s,
                label = "Profile",
                isSelected = currentRoute == NamePage.PROFILE,
                onClick = { onNavigate(NamePage.PROFILE) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: Int,
    selectedIcon: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Icon(
            painter = painterResource(if (isSelected) selectedIcon else icon),
            contentDescription = label,
            tint = if (isSelected) Color(0xFFE94560) else Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) Color(0xFFE94560) else Color.White.copy(alpha = 0.8f),
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
