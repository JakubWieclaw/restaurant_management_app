package com.example.restaurantmanagementapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.R

val fontFamily = FontFamily(
    Font(R.font.podkova_regular, FontWeight.Normal),
    Font(R.font.podkova_medium, FontWeight.Medium),
    Font(R.font.podkova_semibold, FontWeight.SemiBold),
    Font(R.font.podkova_bold, FontWeight.Bold),
)
// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontSize = 34.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
    ),
    displayMedium= TextStyle(
        fontSize = 26.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = fontFamily,
    ),
    displaySmall= TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = fontFamily,
    ),
    headlineLarge= TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
    ),
    headlineMedium= TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
    ),
    headlineSmall= TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
    ),
    titleLarge= TextStyle(
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
    ),
    titleMedium= TextStyle(
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
    ),
    titleSmall= TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
    ),
    bodyLarge= TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = fontFamily,
    ),
    bodyMedium= TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = fontFamily,
    ),
    bodySmall= TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = fontFamily,
    ),
    labelLarge= TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = fontFamily,
    ),
    labelMedium= TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = fontFamily,
    ),
    labelSmall= TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = fontFamily,
    )
)
