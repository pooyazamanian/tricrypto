package com.example.tradeapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    placeholder: String? = null
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = Color.White, // Max contrast
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium),
            placeholder = placeholder?.let { { Text(it, color = Color.White.copy(alpha = 0.5f)) } },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE94560),
                unfocusedBorderColor = Color.White.copy(alpha = 0.35f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.15f),
                focusedContainerColor = Color.White.copy(alpha = 0.2f),
                disabledBorderColor = Color.White.copy(alpha = 0.2f),
                disabledTextColor = Color.White.copy(alpha = 0.5f),
                cursorColor = Color(0xFFE94560),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = singleLine
        )
    }
}
