package com.example.restaurantmanagementapp.CartScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.classes.OrderViewModel


@Composable
fun CartScreen(orderViewModel: OrderViewModel) {
    var promoCode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Text(text="Twój koszyk: ",modifier = Modifier.height(24.dp), fontSize = 20.sp)
        Divider(
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        // MealCartCard list
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(orderViewModel.orderItems) { index, cartItem ->
                MealCartCard(
                    orderViewModel = orderViewModel,
                    index = index,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Promo code section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color= Color.LightGray, shape = RoundedCornerShape(16.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = promoCode,
                onValueChange = { promoCode = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(start=8.dp,end=8.dp)
                    .background(color= Color.LightGray, shape = MaterialTheme.shapes.small),
                textStyle = TextStyle(fontSize = 18.sp)
            )
            Button(
                onClick = { /* Handle apply promo code */ },
                modifier = Modifier.background(color = Color.Yellow).clip(RoundedCornerShape(topStart=0.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 16.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
            ) {
                Text(text = "Apply", style = TextStyle(color= Color.Black), fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cart summary
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val itemprice = orderViewModel.getOrderTotal()
            val addons = 20.0
            val discount = 20.0
            CartSummaryItem(label = "Item Price", price = itemprice)
            CartSummaryItem(label = "Addons", price = addons)
            Divider(
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            CartSummaryItem(label = "Subtotal", price = itemprice+addons)
            CartSummaryItem(label = "Discount", price = -discount)
            Divider(
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            CartSummaryItem(label = "Total", price = itemprice+addons-discount, isTotal = true)
        }
    }
}
@Composable
fun CartSummaryItem(label: String, price: Double, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 20.sp else 16.sp,
            color = if (isTotal) Color.Black else Color.Gray
        )
        Text(
            text = "${String.format("%.2f", price)} zł",
            fontSize = if (isTotal) 20.sp else 16.sp,
            color = if (isTotal) Color.Black else Color.Gray
        )
    }
}
