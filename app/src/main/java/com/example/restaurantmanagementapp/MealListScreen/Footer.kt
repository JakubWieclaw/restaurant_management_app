package com.example.restaurantmanagementapp.MealListScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.classes.OrderViewModel

//@Composable
//fun Footer(
//    orderViewModel: OrderViewModel,
//    modifier: Modifier = Modifier,
//    navController: NavController
//){
//
//    Row(modifier = Modifier
//        .fillMaxWidth()
//        .then(modifier), horizontalArrangement = Arrangement.End){
//
//        Button(
//            onClick = {
//                navigateToScreen("cart",navController)
//            },
//            modifier = modifier.padding(12.dp)
//        ) {
//            Row(modifier = Modifier.align(Alignment.CenterVertically)){
//                Icon(
//                    imageVector = Icons.Default.ShoppingCart,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .padding(end = 8.dp)
//                        .size(32.dp)
//                        .align(Alignment.CenterVertically),
//                    tint = Color.Black
//                )
//                Text("${orderViewModel.getSize()}: ${"%.2f".format(orderViewModel.getOrderTotal())} ZŁ", textAlign = TextAlign.Center, modifier = Modifier.align(
//                    Alignment.CenterVertically))
//            }
//
//        }
//    }
//}