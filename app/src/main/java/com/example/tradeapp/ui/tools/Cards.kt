package com.example.tradeapp.ui.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradeapp.R

//خدمات صفحه اصلی
@Composable
fun BaseCard(
    buttonColor: Color = Color.Transparent,
    borderColor: Color = MaterialTheme.colorScheme.background.copy(alpha = 0.3f),

    onclick: () -> Unit,
    liveContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable {
                onclick()
            }
            .clip(RoundedCornerShape(5.dp))
            .wrapContentHeight()
            .wrapContentWidth()
            .border(
                1.dp,
                borderColor,
                shape = RoundedCornerShape(5.dp)
            )
            .background(buttonColor),
    ) {
        Column(
            modifier = Modifier
                .background(Color.Transparent)
                .wrapContentSize(),//** set height for service button and it would be not ok with long text**
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            liveContent()
        }
    }

}


@Preview
@Composable
fun MainCoinCard() {
    BaseCard(
        buttonColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
        onclick = {},
        borderColor =  MaterialTheme.colorScheme.primary,
        liveContent = {
            Column(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            ) {
                Spacer(Modifier.height(10.dp))

                //            if(image != ""){
//                Image(
//                    painter = rememberAsyncImagePainter(image),
//                    contentDescription = "service",
//                    modifier = imageModifier,
//                    contentScale = ContentScale.FillBounds
//                )
//            }

//            else if(imageLocal != 0){
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.test),
                        contentDescription = "service",
                        modifier = Modifier.size(30.dp),
                        contentScale = ContentScale.FillBounds
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "BITCOIN",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 16.sp,
                        maxLines = 2,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(5.dp))
                Text(
                    text = "8600",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 15.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "+50 (+3.23%)",
                    color = MaterialTheme.colorScheme.surfaceTint,
                    fontSize = 13.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(5.dp))
            }
        }
    )
}


@Preview
@Composable
fun OtherCoinCard() {
    BaseCard(
        buttonColor = MaterialTheme.colorScheme.background.copy(alpha = 0.05f),
        onclick = {},
        liveContent = {
            Column(
                modifier = Modifier.padding(start = 10.dp, end = 25.dp)
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(5.dp)
                            )
                            .background(Color.Blue)
                            .wrapContentSize()

                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "NFLX",
                            color = MaterialTheme.colorScheme.background,
                        fontSize = 13.sp,
                            maxLines = 2,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Netflix, Inc",
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        maxLines = 2,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(5.dp))
                Text(
                    text = "86008600",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 16.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "+50 (+3.23%)",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 7.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(10.dp))

            }
        }
    )
}


@Preview
@Composable
fun StateCard() {
    BaseCard(
        buttonColor = MaterialTheme.colorScheme.background.copy(alpha = 0.05f),
        onclick = {},
        liveContent = {

            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(5.dp)
                        )
                        .background(MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.3f))
                        .wrapContentSize()
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        modifier = Modifier.rotate(90f),
                        contentDescription = ""
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = "sdfsdf",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 16.sp,
                        maxLines = 2,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "+50 (+3.23%)",
                        color = MaterialTheme.colorScheme.surfaceTint,
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
    )
}


@Preview
@Composable
fun StateBoxCard() {

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
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.FillBounds
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
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
                Text(
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
        Row {
            Row(
                modifier = Modifier
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "8600",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 15.sp,
                        maxLines = 2,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "+50 (+3.23%)",
                        color = MaterialTheme.colorScheme.surfaceTint,
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

    }


}


