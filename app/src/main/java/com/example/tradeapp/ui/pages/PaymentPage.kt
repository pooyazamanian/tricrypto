package com.example.tradeapp.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tradeapp.R
import com.example.tradeapp.ui.components.*
import com.example.tradeapp.utils.NamePage

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PaymentPage(
    navigation: NavHostController
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = "روش پرداخت",
            color = colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        GlassCard(opacity = 0.1f) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navigation.navigate(NamePage.PAYMENT) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "انتخاب شده",
                    color = colorScheme.tertiary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "کارت بانکی", color = colorScheme.onSurface)
                    Spacer(Modifier.width(12.dp))
                    Image(
                        painter = painterResource(R.drawable.bankcard),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(CircleShape)
                .background(colorScheme.onSurface.copy(alpha = 0.05f))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            StepIndicator(number = "1", isSelected = true)
            Text(
                text = "انتخاب مبلغ",
                color = colorScheme.tertiary,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(32.dp).height(2.dp).background(colorScheme.onSurface.copy(alpha = 0.2f)))
            StepIndicator(number = "2", isSelected = false)
            Text(
                text = "پرداخت",
                color = colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "مبلغ شارژ را انتخاب کنید",
            color = colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
//            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(6) {
                SelectionAmountCard(amount = "2000")
            }
        }

        GlassButton(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), 
            text = "خرید",
            onClick = { navigation.navigate(NamePage.FINAL_PAYMENT) }
        )
    }
}

@Composable
private fun StepIndicator(number: String, isSelected: Boolean) {
    val colorScheme = MaterialTheme.colorScheme
    
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(if (isSelected) colorScheme.tertiary else Color.Transparent)
            .border(1.dp, colorScheme.onSurface.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            color = colorScheme.onSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SelectionAmountCard(amount: String) {
    val colorScheme = MaterialTheme.colorScheme
    
    GlassCard(opacity = 0.1f) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = amount, color = colorScheme.onSurface, fontWeight = FontWeight.Bold)
        }
    }
}
