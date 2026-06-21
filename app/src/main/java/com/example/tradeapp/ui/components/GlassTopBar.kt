package com.example.tradeapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradeapp.R

@Composable
fun GlassTopBar() {
    val colorScheme = MaterialTheme.colorScheme
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.onSurface.copy(alpha = 0.12f))
            .drawBehindBorder(colorScheme.onSurface.copy(alpha = 0.2f))
            .statusBarsPadding()
            .padding(vertical = 12.dp)
    ) {
        // App Logo and Title Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.menu),
                contentDescription = "Menu",
                modifier = Modifier.size(26.dp).clickable { }
            )
            
            Image(
                painter = painterResource(id = R.drawable.title),
                contentDescription = "Title",
                modifier = Modifier.wrapContentWidth().height(32.dp),
                contentScale = ContentScale.Fit
            )
            
            Image(
                painter = painterResource(R.drawable.setting),
                contentDescription = "Settings",
                modifier = Modifier.size(26.dp).clickable { }
            )
        }
    }
}

@Composable
private fun SelectionChip(label: String) {
    val colorScheme = MaterialTheme.colorScheme
    
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(colorScheme.onSurface.copy(alpha = 0.2f))
            .border(1.dp, colorScheme.onSurface.copy(alpha = 0.3f), CircleShape)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = colorScheme.onSurface,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StatusCard(title: String, value: String) {
    val colorScheme = MaterialTheme.colorScheme
    
    Box(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(colorScheme.onSurface.copy(alpha = 0.15f))
            .border(1.dp, colorScheme.onSurface.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorScheme.tertiary.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = colorScheme.tertiary,
                    modifier = Modifier.size(18.dp).rotate(90f)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = value,
                    color = colorScheme.tertiary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

private fun Modifier.drawBehindBorder(color: Color): Modifier = this.then(
    Modifier.drawWithContent {
        drawContent()
        drawLine(
            color = color,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = 1.dp.toPx()
        )
    }
)
