package com.example.tradeapp.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tradeapp.R
import com.example.tradeapp.ui.tools.MainImportantButton
import com.example.tradeapp.ui.tools.OutLineImportantButton
import com.example.tradeapp.ui.tools.OutLineImportantWithImageButton
import com.example.tradeapp.utils.NamePage

@Composable
fun PaymentPage(
    navigation: NavHostController
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier
                .padding(15.dp), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ){
            Text(
                text = "روش پرداخت",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            OutLineImportantWithImageButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                "fjashf",
                Color.White,
                R.drawable.bankcard,
                onclick = {
                    navigation.navigate(NamePage.PAYMENT)
                }

            ) {
                Text(
                    text = "انتخاب شده",
                    color = MaterialTheme.colorScheme.surfaceTint,
                    fontSize = 14.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.5f))
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            Row(
                modifier = Modifier
                    .border(1.dp, Color.White, CircleShape)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceTint),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "1",
                    color = Color.White,
                    fontSize = 18.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text = "انتخاب مبلغ",
                color = MaterialTheme.colorScheme.surfaceTint,
                fontSize = 14.sp,
                maxLines = 2,
                modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.padding(10.dp).width(50.dp).height(2.dp).background(MaterialTheme.colorScheme.surface))
            Row(
                modifier = Modifier
                    .border(1.dp, Color.White, CircleShape)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "2",
                    color = Color.White,
                    fontSize = 18.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text = "پرداخت",
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(5.dp),
                fontSize = 14.sp,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                overflow = TextOverflow.Ellipsis
            )

        }
        Column(
            Modifier.fillMaxWidth()
                .padding(15.dp), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        )
        {
            Text(
                text = "مبلغ شارژ را انتخاب کنید",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )

        }
        val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp -30.dp
        LazyVerticalGrid(
            columns = GridCells.Adaptive(screenWidthDp/3),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max= 1000.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            items(6) {
                OutLineImportantButton(
                    modifier = Modifier.padding(10.dp),
                    color = Color.White,
                    text = "2000"

                )
            }

        }
        MainImportantButton(modifier = Modifier.padding(20.dp).fillMaxWidth(), text = "خرید"){
            navigation.navigate(NamePage.FINAL_PAYMENT)
        }

    }




}