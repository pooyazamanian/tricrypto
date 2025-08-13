package com.example.tradeapp.ui.pages

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tradeapp.R
import com.example.tradeapp.ui.tools.MainCard
import com.example.tradeapp.ui.tools.MainImportantButton
import com.example.tradeapp.ui.tools.RowContent
import com.example.tradeapp.utils.NamePage

@Composable
fun ProfilePage(
    navigation: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(0.8f), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(R.drawable.test),
                contentDescription = "profile",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(150.dp).clip(CircleShape)
            )
            Spacer(Modifier.size(20.dp))
            Column(modifier = Modifier) {
                Column(modifier = Modifier) {
                    Text(
                        text = "پیش پرداخت",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        //            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "یکشنبه، ۳۰ اکتبر ۲۰۲۲ ساعت ۱:۳۲ بعد از ظهر، WIB",
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.End,
                        //            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Spacer(Modifier.size(20.dp))

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
            }
        }
        Spacer(Modifier.size(20.dp))

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
            }
        }
        MainImportantButton(modifier = Modifier.padding(20.dp).fillMaxWidth(), text = "ویرایش پروفایل"){
            navigation.navigate(NamePage.PROFILE_EDITOR)
        }
    }
}