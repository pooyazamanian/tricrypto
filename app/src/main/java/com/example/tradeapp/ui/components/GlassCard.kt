package com.example.tradeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tradeapp.ui.theme.glassColors

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    opacity: Float = 0.12f,
    content: @Composable () -> Unit
) {
    val glassColors = MaterialTheme.glassColors
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .background(glassColors.cardBackground.copy(alpha = opacity))
            .border(1.dp, glassColors.cardBorder.copy(alpha = 0.2f), RoundedCornerShape(cornerRadius))
    ) {
        content()
    }
}
