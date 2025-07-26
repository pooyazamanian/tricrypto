package com.example.tradeapp.ui.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import com.example.tradeapp.R
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.Image
import org.w3c.dom.Text


@Composable
fun BaseButton(
    modifier: Modifier,
    height: Dp = 50.dp,
    width: Dp = 190.dp,
    elevation: Dp = 10.dp,
    shape: Shape = RoundedCornerShape(10.dp),
    color: Color = MaterialTheme.colorScheme.tertiary,
    onclick: () -> Unit,
    insideItems: @Composable() () -> Unit
) {

    Column(
        modifier = modifier
            .clickable {
                onclick()
            }
            .height(height)
            .width(width)
            .let{
                if(elevation == 0.dp){
                    it
                }else{
                    it.shadow(shape = shape, elevation = elevation)
                }

            }
            .background(color, shape = shape)
            .clip(shape),
    ) {
        insideItems()
    }
}


@Composable
fun OutLineImportantButton(
    text: String,
    color: Color
    ) {
    BaseButton(
        Modifier
            .border(1.dp, color, RoundedCornerShape(10.dp)),
        onclick = {},
        //we need to set 0.dp to remove shadow for get alpha color
        elevation = 0.dp,
        color = color.copy(alpha = 0.2f)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.background,
                fontSize = 16.sp,
                maxLines = 2,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}

@Composable
fun MainImportantButton(
    text: String,
) {
    BaseButton(Modifier, onclick = {}) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.background,
                fontSize = 16.sp,
                maxLines = 2,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}


@Composable
fun MainImportantIconButton(
    text: String,
    image: Int,
    color: Color = MaterialTheme.colorScheme.background,
    modifier: Modifier = Modifier
) {
    BaseButton(modifier, onclick = {}) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(image),
                modifier = Modifier.size(30.dp),
                contentDescription = "Icon"
            )
            Spacer(Modifier.size(5.dp))
            Text(
                text = text,
                color = color,
                fontSize = 16.sp,
                maxLines = 2,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun OptionButton(){
    Row(
        modifier = Modifier.background(Color.Transparent)
            .clip(CircleShape)
            .clickable { }
            .border(1.dp, Color.White, CircleShape)
            .padding(vertical = 5.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "selectedOption",
            color = Color.White,
            fontSize = 13.sp,
            maxLines = 2,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign  .Center,
//            fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
            overflow = TextOverflow.Ellipsis
        )
    }
}