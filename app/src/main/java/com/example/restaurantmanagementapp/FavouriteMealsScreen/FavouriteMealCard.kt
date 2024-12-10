package com.example.restaurantmanagementapp.FavouriteMealsScreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.MealDetailsScreen.StarRating
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.apithings.schemasclasses.Meal
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.loadImageFromDevice
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun FavouriteMealCard(
    meal: Meal,
    onAddToCart: (Meal) -> Unit,
    onDelete: (Meal) -> Unit
) {
    val maxOffsetX = -200f // Przesuwanie w lewo (wartość ujemna)
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val imageBitmap: ImageBitmap = loadImageFromDevice(context = context, filename = meal.photographUrl)
        ?: ImageBitmap.imageResource(id = R.drawable.no_photo)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Zapewnia, że Box dopasuje się do wysokości zawartości
            .background(Color.Red, RoundedCornerShape(8.dp))
    ){
        IconButton(
            onClick = { onDelete(meal) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(50.dp)
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Icon", tint = Color.White, modifier = Modifier.size(50.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            // Jeśli użytkownik nie przesunął wystarczająco daleko, karta wraca na miejsce
                            coroutineScope.launch {
                                if (offsetX.value > maxOffsetX / 2) {
                                    offsetX.animateTo(0f, tween(durationMillis = 300))
                                } else {
                                    offsetX.animateTo(maxOffsetX, tween(durationMillis = 300))
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            // Aktualizacja przesunięcia podczas przeciągania
                            val newOffsetX = (offsetX.value + dragAmount.x).coerceIn(maxOffsetX, 0f)
                            coroutineScope.launch {
                                offsetX.snapTo(newOffsetX)
                            }
                            change.consume()
                        }
                    )
                }
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image with rounded corners
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Meal Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    // Item Name
                    Text(text = meal.name, style = Typography.labelLarge, maxLines = 2)

                    // Rating stars
                    StarRating(rating = meal.avgRating.toInt(), size=16.dp)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Total price
                    Text(text = stringResource(id =  R.string.total) +" ${String.format("%.2f", meal.price)} " + stringResource(
                        id = R.string.currency
                    ), style = Typography.bodyMedium)
                }

                Spacer(modifier = Modifier.width(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    IconButton(
                        onClick = {
                            onAddToCart(meal)
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            }
        }
    }
}