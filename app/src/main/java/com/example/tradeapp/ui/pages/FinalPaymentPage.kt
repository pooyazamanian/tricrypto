package com.example.tradeapp.ui.pages

import android.util.EventLogTags
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradeapp.R
import androidx.navigation.NavHostController
import com.example.tradeapp.ui.tools.MainCard
import com.example.tradeapp.ui.tools.MainImportantButton
import com.example.tradeapp.utils.NamePage

@Composable
fun FinalPaymentPage(navigator: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(painter = painterResource(R.drawable.logopayment), contentDescription = "")
        Text(
            text = "لطفا فرایند پرداخت را تکمیل کنید",
            color = MaterialTheme.colorScheme.surfaceTint,
            fontSize = 16.sp,
            maxLines = 2,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            //            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
            overflow = TextOverflow.Ellipsis
        )
        Spacer(
            Modifier.size(30.dp)
        )
        TitleTImer()
        Spacer(
            Modifier.size(10.dp)
        )
        ContentOfPayment()
        Spacer(
            Modifier.size(10.dp)
        )
        DescriptionPart()
        Spacer(
            Modifier.size(10.dp)
        )
        MainImportantButton(modifier = Modifier.padding(20.dp).fillMaxWidth(), text = "خرید"){
        }
    }
}


@Composable
fun TitleTImer() {
    MainCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                Text(
                    text = "پیش پرداخت",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 13.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    //            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "یکشنبه، ۳۰ اکتبر ۲۰۲۲، ساعت ۱:۳۲ بعد از ظهر، WIB",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 10.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    //            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                    overflow = TextOverflow.Ellipsis
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.error,
                        RoundedCornerShape(10.dp)
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "00 : 08 : 46",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 13.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    //            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                    overflow = TextOverflow.Ellipsis
                )
            }


        }
    }
}

@Composable
fun DescriptionPart() {
    MainCard {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "راهنمای پرداخت",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 13.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    //            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "کد پرداخت بالا را یادداشت کنید و به نزدیکترین شعبه آلفامارت، آلفا میدی، لاوسون یا DAN+DAN مراجعه کنید.\n" +
                            "\n" +
                            "به صندوقدار اطلاع دهید که می\u200Cخواهید پرداخت انجام دهید.\n" +
                            "\n" +
                            "صندوقدار کد پرداخت را درخواست می\u200Cکند. کد پرداخت خود را ارائه دهید: 11111102XXXXXX. صندوقدار اطلاعات مشتری، از جمله نام فروشنده، نام مشتری و مبلغ را تأیید می\u200Cکند. مبلغ مشخص شده را به صندوقدار پرداخت کنید.\n" +
                            "\n" +
                            "رسید را به عنوان مدرک پرداخت موفقیت\u200Cآمیز دریافت کنید. فروشنده بلافاصله اعلان پرداخت را دریافت خواهد کرد.\n" +
                            "\n" +
                            "تکمیل شد",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Justify,
                    //            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                    overflow = TextOverflow.Ellipsis
                )
            }
    }
}


@Composable
fun ContentOfPayment() {
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
}


@Composable
private fun RowContent(title: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.background,
            fontSize = 13.sp,
            maxLines = 2,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            //            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = content,
            color = MaterialTheme.colorScheme.background,
            fontSize = 10.sp,
            maxLines = 2,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            //            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
            overflow = TextOverflow.Ellipsis
        )

    }

}