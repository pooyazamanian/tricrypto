package com.example.tradeapp.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradeapp.R
import com.example.tradeapp.ui.tools.OutLineImportantWithImageButton
import com.example.tradeapp.utils.NamePage

@Composable
fun TopUpPage(
    navigation: NavHostController
) {
    Column(Modifier.fillMaxSize().padding(15.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End) {
        Text(
            text = "روش را انتخاب کنید:",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 16.sp,
            maxLines = 2,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
        for (i in 1..3) {
            OutLineImportantWithImageButton(
                modifier = Modifier
                    .fillMaxWidth().padding(vertical = 10.dp),
                "fjashf",
                Color.White,
                R.drawable.bankcard,
                onclick = {
                    navigation.navigate(NamePage.PAYMENT)
                }

            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "icon",
                    tint = Color.White
                )
            }

        }
    }

}