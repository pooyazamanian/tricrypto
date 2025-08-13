package com.example.tradeapp.ui.tools

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SimpleTextFiled(text: MutableState<String>,modifier: Modifier) {
    OutlinedTextField(
        value = text.value,
        modifier = modifier, colors = OutlinedTextFieldDefaults.colors().copy(
            unfocusedContainerColor = Color.White.copy(alpha = 0.15f),
            unfocusedLabelColor = Color.White,
            unfocusedIndicatorColor = Color.White
        ),
        shape = RoundedCornerShape(15.dp),
        onValueChange = { text.value = it },
        label = { Text("Enter Your Name") }
    )
}