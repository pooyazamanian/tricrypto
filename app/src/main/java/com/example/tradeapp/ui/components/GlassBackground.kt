package com.example.tradeapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.example.tradeapp.ui.theme.glassColors

@Composable
fun GlassBackground(
    content: @Composable () -> Unit
) {
    val glassColors = MaterialTheme.glassColors
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        glassColors.backgroundGradientStart,
                        glassColors.backgroundGradientMiddle,
                        glassColors.backgroundGradientEnd
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = glassColors.backgroundCirclePink.copy(alpha = 0.2f),
                radius = size.minDimension * 0.4f,
                center = Offset(x = 0f, y = 0f)
            )
            drawCircle(
                color = glassColors.backgroundCircleBlue.copy(alpha = 0.35f),
                radius = size.minDimension * 0.6f,
                center = Offset(x = size.width, y = size.height * 0.7f)
            )
        }
        content()
    }
}
