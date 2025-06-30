package com.example.tradeapp.ui.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.tradeapp.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun NetWorkConnectionDialog( onDismissRequest: () -> Unit = {}) {
    Dialog(onDismissRequest = {
        onDismissRequest()
    }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background (Color.Transparent),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp)
                    )
                    .clip(RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    text = "اتصال به اینترنت",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, bottom = 10.dp),
                    textAlign = TextAlign .Right,
                    fontWeight = FontWeight(700),
                    fontSize = 32.sp
                )
                Image(
                    painter = painterResource(id = R.drawable.notconnected),
                    contentDescription = "no internet",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "به اینترنت متصل نیستید. برای استفاده از اپلیکیشن به اینترنت متصل شوید",
                    fontWeight = FontWeight(400),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

}