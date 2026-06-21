package com.example.tradeapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GlassBackground(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E),
                        Color(0xFF0F3460)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(0xFFE94560).copy(alpha = 0.2f), // Increased opacity
                radius = size.minDimension * 0.4f,
                center = Offset(x = 0f, y = 0f)
            )
            drawCircle(
                color = Color(0xFF0F3460).copy(alpha = 0.35f), // Increased opacity
                radius = size.minDimension * 0.6f,
                center = Offset(x = size.width, y = size.height * 0.7f)
            )
        }
        content()
    }
}
