package com.example.restaurantmanagementapp.CartScreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.MealDetailsScreen.StarRating
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.CouponsViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderViewModel
import kotlinx.coroutines.launch
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun MealCartCard(
    orderViewModel: OrderViewModel,
    couponsViewModel: CouponsViewModel,
    index: Int,
    onEditClick: () -> Unit,
) {
    val cartItem = orderViewModel.orderItems[index]
    //var quantity by remember { mutableIntStateOf(cartItem.quantity) }
    val maxOffsetX = -200f // Przesuwanie w lewo (wartość ujemna)
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val selectedCoupon = couponsViewModel.selectedCoupon
    val isDiscount = selectedCoupon!=null && selectedCoupon.meal.id == cartItem.id


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Zapewnia, że Box dopasuje się do wysokości zawartości
            .background(Color.Red, RoundedCornerShape(8.dp))
    ) {
        IconButton(
            onClick = { orderViewModel.deleteFromOrder(cartItem) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(50.dp)
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Icon", tint = Color.White, modifier = Modifier.size(50.dp))
        }

        // Przesuwana karta
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
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.test_meal_picture_1),
                    contentDescription = "Meal Image",
                    modifier = Modifier
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .weight(0.2f),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopStart
                )

                Column(modifier = Modifier.weight(0.45f).padding(horizontal=10.dp)) {
                    Text(text = cartItem.name, style = Typography.labelLarge, maxLines = 2)
                    StarRating(rating = round(cartItem.avgRating).roundToInt(), size = 18.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    cartItem.removedIngredients.forEach { rIngredient ->
                        Text(" - $rIngredient", style = Typography.bodySmall)
                    }
                    Text(text = stringResource(id = R.string.total) +" ${String.format("%.2f", cartItem.price * cartItem.quantity)}" + stringResource(id = R.string.currency), style = Typography.bodyMedium, textDecoration = if(isDiscount) TextDecoration.LineThrough else TextDecoration.None,  color = if(isDiscount) Color.Red else Color.Black)
                    if(isDiscount&&selectedCoupon!=null){
                        Text(text = String.format("%.2f", cartItem.price * cartItem.quantity* (100.0-selectedCoupon.discountPercentage)/100.0), style = Typography.bodyMedium)
                    }

                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .height(120.dp)
                        .weight(0.35f)
                ) {

                    Text(text = cartItem.quantity.toString(), style = Typography.titleMedium)

                    Column(verticalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxHeight()){
                        IconButton(
                            onClick = {
                                orderViewModel.updateQuantity(index, cartItem.quantity+1)
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(5))
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase Quantity")
                        }
                        IconButton(
                            onClick = {
                                orderViewModel.updateQuantity(index, cartItem.quantity-1)
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(5))
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease Quantity")
                        }


                    }

                    IconButton(onClick={onEditClick()}){
                        Icon(Icons.Default.List, contentDescription = "Edit")
                    }
                }
            }
        }
    }
}
