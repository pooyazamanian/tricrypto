package com.example.tradeapp.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tradeapp.ui.tools.MainImportantIconButton
import java.nio.file.WatchEvent
import com.example.tradeapp.R
import com.example.tradeapp.ui.tools.MainCoinCard
import com.example.tradeapp.ui.tools.StateBoxCard


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HomePage(
    navigation: NavHostController
) {


    Column(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                Text(
                    text = "حساب شما",
                    color = Color.White,
                    fontSize = 20.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier.rotate(90f),
                    contentDescription = ""
                )
                Spacer(Modifier.size(5.dp))

                Text(
                    text = "5000",
                    color = MaterialTheme.colorScheme.surfaceTint,
                    fontSize = 20.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.size(5.dp))

                Text(
                    text = "(100%)",
                    color = MaterialTheme.colorScheme.surfaceTint,
                    fontSize = 20.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )

            }
            Spacer(Modifier.height(15.dp))

            val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp - 50.dp
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                MainImportantIconButton(
                    text = "برداشت",
                    color = Color.White,
                    modifier = Modifier.width(screenWidthDp / 2),
                    image = R.drawable.cardsend
                ){}
                Spacer(Modifier.width(5.dp))
                MainImportantIconButton(
                    text = "پرداخت",
                    color = Color.White,
                    modifier = Modifier.width(screenWidthDp / 2),
                    image = R.drawable.cardreceive
                ){}
            }
            Spacer(Modifier.height(25.dp))
            Text(
                text = "ترند های امروز:",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(10.dp))

            LazyRow {
                items(5) {
                    MainCoinCard()
                    Spacer(Modifier.width(5.dp))
                }
            }
            Spacer(Modifier.height(25.dp))
            Text(
                text = "فهرست پیگیری های من:",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            for (i in 1..10){
                StateBoxCard()
            }
//        Button() { }
        }

    }

}