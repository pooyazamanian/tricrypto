package com.example.tradeapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tradeapp.ui.theme.glassColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassPullToRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val state = rememberPullToRefreshState()
    val glassColors = MaterialTheme.glassColors
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .pullToRefresh(
                state = state,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh
            )
    ) {
        content()
        
        PullToRefreshDefaults.Indicator(
            state = state,
            isRefreshing = isRefreshing,
            modifier = Modifier.align(Alignment.TopCenter),
            containerColor = glassColors.pullToRefreshContainer.copy(alpha = 0.9f),
            color = glassColors.pullToRefreshContent
        )
    }
}
