package com.example.restaurantmanagementapp.MealListScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.classes.Meal
import com.example.restaurantmanagementapp.ui.theme.Typography

@Composable
fun MealCard(meal: Meal, onAddToOrder: (Meal) -> Unit, modifier: Modifier, navController: NavController) {
    Column(modifier = modifier.padding(all = 8.dp)) {

        val shape = RoundedCornerShape(30.dp)
        Box(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .background(Color.White, shape = shape),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.test_meal_picture_1),
                contentDescription = "Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 24.dp, end = 24.dp)
            ) {
                OutlinedText(
                    text = meal.name,
                    fillColor = Color.Black,
                    outlineColor = Color.White,
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                OutlinedText(
                    text = meal.price.toString() + " ZŁ",
                    fillColor = Color.Black,
                    outlineColor = Color.White,
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

//                Text(meal.name,
//                    color = Color.White,
//                    modifier = Modifier.background(color=Color.Black),
//                    style = Typography.titleLarge)
                //Text(meal.price.toString(), color = Color.White, modifier = Modifier.background(color=Color.Black), style = Typography.titleLarge)
            }

        }


        Row(
            horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.LightGray,
                    shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier
                    .padding(start = 8.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
                    .align(Alignment.CenterVertically)
                    .weight(0.20f)
            ) {
                Text("2/5", fontSize = 24.sp)
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                    tint = Color.Yellow
                )
            }
            Button(
                onClick = { navigateToScreen("meal/${meal.id}",navController)},
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
                    .weight(0.3f),
            ) {
                Text("details")
            }
            Button(
                onClick = {onAddToOrder(meal)},
                modifier = Modifier
                    .padding(start = 4.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    .weight(0.5f),
            ) {
                Text("add to order", maxLines = 1)
            }
        }
    }
}
