package com.example.tradeapp.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradeapp.R
import androidx.navigation.NavHostController
import com.example.tradeapp.ui.components.*

@Composable
fun FinalPaymentPage(navigator: NavHostController) {
    val colorScheme = MaterialTheme.colorScheme
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logopayment), 
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        
        Text(
            text = "لطفا فرایند پرداخت را تکمیل کنید",
            color = colorScheme.onSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        TitleTimer()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ContentOfPayment()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        DescriptionPart()

        Spacer(modifier = Modifier.height(16.dp))

        GlassButton(
            text = "تایید نهایی",
            onClick = { navigator.popBackStack() }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TitleTimer() {
    val colorScheme = MaterialTheme.colorScheme
    
    GlassCard(opacity = 0.1f) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "زمان باقی‌مانده",
                    color = colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
                Text(
                    text = "یکشنبه، ۳۰ اکتبر ۲۰۲۲",
                    color = colorScheme.onSurface.copy(alpha = 0.4f),
                    fontSize = 11.sp
                )
            }
            Text(
                text = "08:46",
                color = colorScheme.primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun DescriptionPart() {
    val colorScheme = MaterialTheme.colorScheme
    
    GlassCard(opacity = 0.08f) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = colorScheme.tertiary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "راهنمای پرداخت",
                    color = colorScheme.onSurface,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "کد پرداخت بالا را یادداشت کنید و به نزدیکترین شعبه مراجعه کنید. مبالغ را به دقت بررسی کنید.",
                color = colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun ContentOfPayment() {
    val colorScheme = MaterialTheme.colorScheme
    
    GlassCard(opacity = 0.12f) {
        Column(modifier = Modifier.padding(16.dp)) {
            PaymentRow(label = "شماره معامله", value = "FD8U4J38J004093J8U80TJ039")
            Divider(color = colorScheme.onSurface.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 12.dp))
            PaymentRow(label = "میزان معامله", value = "2,000,000 تومان")
            Divider(color = colorScheme.onSurface.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 12.dp))
            PaymentRow(label = "کد پرداخت", value = "FD8U4J38TJ039")
        }
    }
}

@Composable
private fun PaymentRow(label: String, value: String) {
    val colorScheme = MaterialTheme.colorScheme
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = colorScheme.onSurface.copy(alpha = 0.6f), fontSize = 14.sp)
        Text(text = value, color = colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun Divider(color: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().height(1.dp).background(color))
}
