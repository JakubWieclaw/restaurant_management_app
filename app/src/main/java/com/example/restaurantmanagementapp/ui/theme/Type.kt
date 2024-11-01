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
        color = Color.Black,
        fontSize = 34.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    displayMedium= TextStyle(
        color = Color.Black,
        fontSize = 26.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    displaySmall= TextStyle(
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    headlineLarge= TextStyle(
        color = Color.Black,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    headlineMedium= TextStyle(
        color = Color.Black,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    headlineSmall= TextStyle(
        color = Color.Black,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    titleLarge= TextStyle(
        color = Color.Black,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    titleMedium= TextStyle(
        color = Color.Black,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    titleSmall= TextStyle(
        color = Color.Black,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    bodyLarge= TextStyle(
        color = Color.Black,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    bodyMedium= TextStyle(
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    bodySmall= TextStyle(
        color = Color.Black,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    labelLarge= TextStyle(
        color = Color.Black,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    labelMedium= TextStyle(
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    ),
    labelSmall= TextStyle(
        color = Color.Black,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = fontFamily,
        letterSpacing = 0.sp,
        textDecoration = null,
        textAlign = TextAlign.Start,
    )
)
