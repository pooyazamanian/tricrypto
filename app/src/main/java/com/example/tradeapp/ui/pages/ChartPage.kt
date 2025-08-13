package com.example.tradeapp.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.example.tradeapp.R
import com.example.tradeapp.ui.tools.MainCard
import com.example.tradeapp.ui.tools.MainImportantButton
import com.example.tradeapp.ui.tools.RowContent
import com.example.tradeapp.ui.tools.charts.JetpackComposeGoldPrices

@Composable
fun ChartPage(
    navigation: NavHostController
) {

    Column(
        modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.test),
                    contentDescription = "service",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(Modifier.width(10.dp))
                Column {
                    androidx.compose.material3.Text(
                        text = "BBRI",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 16.sp,
                        maxLines = 2,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    androidx.compose.material3.Text(
                        text = "Bank Rakyat Indonesia",
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        maxLines = 2,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start

        ) {
            Text(
                text = "تومان",
                color = Color.White,
                fontSize = 30.sp,
                maxLines = 2,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.size(5.dp))
            Text(
                text = "1000000000",
                color = Color.White,
                fontSize = 40.sp,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentWidth(),
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
                    .background(MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.3f))
                    .wrapContentSize().padding(5.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier.rotate(90f),
                    contentDescription = ""
                )
                Text(
                    text = "+3.23%",
                    color = MaterialTheme.colorScheme.surfaceTint,
                    fontSize = 13.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = "+50",
                    color = MaterialTheme.colorScheme.surfaceTint,
                    fontSize = 13.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))

            }
        }
//        / دکمه‌ها برای تغییر بازه زمانی
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {

            }) {
                Text("۱ روز قبل")
            }
            Button(onClick = {

            }) {
                Text("۱ روز")
            }
            Button(onClick = {

            }) {
                Text("۵ روز")
            }
            Button(onClick = {

            }) {
                Text("۱ ماه")
            }
            Button(onClick = {

            }) {
                Text("۵ ماه")
            }
            Button(onClick = {

            }) {
                Text("۱ سال")
            }
            Button(onClick = {

            }) {
                Text("همه")
            }
        }
        JetpackComposeGoldPrices()
        Spacer(Modifier.height(5.dp))
        val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp - 50.dp

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MainImportantButton(
                text = "فروش",
                color = Color.White,
                elevation = 0.dp,
                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3f),
                modifier = Modifier.width(screenWidthDp / 2).border(1.dp, MaterialTheme.colorScheme.error,RoundedCornerShape(10.dp)),
            ){}
            Spacer(Modifier.width(5.dp))
            MainImportantButton(
                text = "خرید",
                elevation = 0.dp,
                color = Color.White,
                modifier = Modifier.width(screenWidthDp / 2),

            ){}
        }
        Spacer(Modifier.height(15.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
            Text(
                text = ":آمار کلیدی",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,

                )
        }
        Spacer(Modifier.height(5.dp))
        MainCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RowContent("شماره معامله", "FD8U4J38J004093J8U80TJ039")
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                RowContent("میزان معامله", "FD8U4J38J004093J0TJ039")
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                RowContent("کد پرداخت", "FD8U4J38TJ039")
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                RowContent("کد پرداخت", "FD8U4J38TJ039")
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                RowContent("کد پرداخت", "FD8U4J38TJ039")
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                RowContent("کد پرداخت", "FD8U4J38TJ039")
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                RowContent("کد پرداخت", "FD8U4J38TJ039")
            }
        }



    }
}