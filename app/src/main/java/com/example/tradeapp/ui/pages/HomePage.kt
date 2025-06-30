package com.example.tradeapp.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun HomePage(
    padding: PaddingValues,
    navigation: NavHostController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = padding.calculateTopPadding(),
//                bottom = padding.calculateBottomPadding() -20.dp
            )
    ) {

        Button() { }
        Spacer(Modifier.height(padding.calculateBottomPadding() + 10.dp))
    }
}