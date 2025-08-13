package com.example.tradeapp.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.tradeapp.R
import com.example.tradeapp.ui.tools.MainCard
import com.example.tradeapp.ui.tools.MainCoinCard
import com.example.tradeapp.ui.tools.MainImportantButton
import com.example.tradeapp.ui.tools.RowContent
import com.example.tradeapp.ui.tools.SimpleTextFiled

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProfileEditorPage(
    navigation: NavHostController
) {


    Column(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var text = remember { mutableStateOf("") }
        val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp - 100.dp

        Row(modifier = Modifier.fillMaxWidth(0.8f), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(R.drawable.test),
                contentDescription = "profile",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(150.dp).clip(CircleShape)
            )
        }


        Spacer(Modifier.size(20.dp))
        Row {
            Column {
                Text(
                    text = "ترند های امروز:",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                SimpleTextFiled(text = text, modifier = Modifier.width(screenWidthDp/2))
            }
            Spacer(Modifier.size(20.dp))

            Column {
                Text(
                    text = "ترند های امروز:",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                SimpleTextFiled(text = text,Modifier.width(screenWidthDp/2))
            }
        }
        Spacer(Modifier.height(10.dp))
        Row {
            Column {
                Text(
                    text = "ترند های امروز:",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                SimpleTextFiled(text = text, modifier = Modifier.width(screenWidthDp))
            }
        }
        Spacer(Modifier.height(10.dp))
        Row {
            Column {
                Text(
                    text = "ترند های امروز:",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                SimpleTextFiled(text = text, modifier = Modifier.width(screenWidthDp))
            }
        }
        Spacer(Modifier.height(10.dp))
        Row {
            Column {
                Text(
                    text = "ترند های امروز:",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                SimpleTextFiled(text = text, modifier = Modifier.width(screenWidthDp/2))
            }
            Spacer(Modifier.size(20.dp))

            Column {
                Text(
                    text = "ترند های امروز:",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                SimpleTextFiled(text = text,Modifier.width(screenWidthDp/2))
            }
        }
        Spacer(Modifier.size(20.dp))

        MainImportantButton(modifier = Modifier.padding(20.dp).fillMaxWidth(), text = "تایید"){
//            navigation.navigate(NamePage.FINAL_PAYMENT)
        }
    }
}