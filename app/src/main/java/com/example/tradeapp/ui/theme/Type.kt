package com.example.tradeapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.sp
import com.example.tradeapp.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        fontWeight = FontWeight.Normal,

        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Justify
    ),
//     Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        fontWeight = FontWeight.Normal,

        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Justify
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        fontWeight = FontWeight.Medium,

        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Justify
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        fontWeight = FontWeight.Medium,

        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Justify
    ),
    // بقیه استایل‌های پیش‌فرض Material3 با فونت سفارشی
    displayLarge = Typography().displayLarge.copy(fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        textAlign = TextAlign.Justify, textDirection = TextDirection.Rtl),
    displayMedium = Typography().displayMedium.copy(fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        textAlign = TextAlign.Justify, textDirection = TextDirection.Rtl),
    displaySmall = Typography().displaySmall.copy(fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        textAlign = TextAlign.Justify, textDirection = TextDirection.Rtl),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        textAlign = TextAlign.Justify, textDirection = TextDirection.Rtl),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        textAlign = TextAlign.Justify, textDirection = TextDirection.Rtl),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        textAlign = TextAlign.Justify, textDirection = TextDirection.Rtl),
    titleMedium = Typography().titleMedium.copy(fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        textAlign = TextAlign.Justify, textDirection = TextDirection.Rtl),
    titleSmall = Typography().titleSmall.copy(fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        textAlign = TextAlign.Justify, textDirection = TextDirection.Rtl),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        textAlign = TextAlign.Justify, textDirection = TextDirection.Rtl),
    bodySmall = Typography().bodySmall.copy(fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        textAlign = TextAlign.Justify, textDirection = TextDirection.Rtl),
    labelLarge = Typography().labelLarge.copy(fontFamily = FontFamily(Font(R.font.iranyekanmedium)),
        textAlign = TextAlign.Justify, textDirection = TextDirection.Rtl)
)