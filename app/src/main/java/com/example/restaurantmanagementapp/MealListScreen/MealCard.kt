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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.apithings.schemasclasses.Meal
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.loadImageFromDevice

@Composable
fun MealCard(meal: Meal, onAddToOrder: (Meal) -> Unit, modifier: Modifier, navController: NavController) {
    val context = LocalContext.current
    val imageBitmap: ImageBitmap = loadImageFromDevice(context = context, filename = meal.photographUrl)
        ?: ImageBitmap.imageResource(id = R.drawable.no_photo)
    Column(modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 12.dp)) {

        val shape = RoundedCornerShape(30.dp)
        Box(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .background(Color.White, shape = shape)
        ) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp, start = 24.dp, end = 24.dp)
            ) {
                OutlinedText(
                    text = meal.name,
                    fillColor = Color.Black,
                    outlineColor = Color.White,
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.65f),
                    maxLines = 2
                )
                OutlinedText(
                    text = meal.price.toString() + " ZŁ",
                    fillColor = Color.Black,
                    outlineColor = Color.White,
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.35f),
                    maxLines = 1
                )
            }

            Row(
                horizontalArrangement = Arrangement.Start, modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 24.dp, top = 10.dp)
                    .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(start = 3.dp, top = 3.dp, bottom = 3.dp, end = 3.dp)
                    .alpha(0.8f)
            ) {
                Text(meal.avgRating.toString(), style = Typography.labelLarge)
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically),
                    tint = Color.Yellow
                )
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
            Button(
                shape = RoundedCornerShape(bottomStart = 30.dp),
                onClick = { navigateToScreen("meal/${meal.id}",navController)},
                modifier = Modifier
                    .padding(start = 2.dp, end = 1.dp, top = 0.dp, bottom = 0.dp)
                    .weight(0.35f),
            ) {
                Text(text= stringResource(R.string.details),style= Typography.labelMedium,maxLines = 1)
            }
            Button(
                shape = RoundedCornerShape(bottomEnd = 30.dp),
                onClick = {onAddToOrder(meal)},
                modifier = Modifier
                    .padding(start = 1.dp, end = 2.dp, top = 0.dp, bottom = 0.dp)
                    .weight(0.65f)
            ) {
                Text(text= stringResource(R.string.add_to_order2),style= Typography.labelMedium, maxLines = 1)
            }
        }
    }
}
