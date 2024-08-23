package com.example.restaurantmanagementapp.CartScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.MealDetailsScreen.StarRating
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.classes.OrderViewModel


@Composable
fun MealCartCard(
    orderViewModel: OrderViewModel,
    index: Int,
) {
    val cartItem = orderViewModel.orderItems[index]
    var quantity by remember { mutableIntStateOf(cartItem.quantity) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image with rounded corners
        Image(
            painter = painterResource(id = R.drawable.test_meal_picture_1),
            contentDescription = "Meal Image",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            // Item Name
            Text(text = cartItem.name, fontSize = 20.sp)

            // Rating stars
            StarRating(rating = 2, size=16.dp)

            Spacer(modifier = Modifier.height(8.dp))

            // Total price
            Text(text = "Total: ${String.format("%.2f", cartItem.price * quantity)} zł", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Quantity selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .width(120.dp)
        ) {
            IconButton(
                onClick = {
                    val newQuantity = (quantity - 1).coerceAtLeast(1)
                    quantity = newQuantity
                    orderViewModel.updateQuantity(index,newQuantity)
                },
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease Quantity")
            }

            Text(text = quantity.toString(), fontSize = 32.sp)

            IconButton(
                onClick = {
                    val newQuantity = (quantity + 1).coerceAtMost(999)
                    quantity = newQuantity
                    orderViewModel.updateQuantity(index,newQuantity)
                },
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase Quantity")
            }
        }
    }
}
