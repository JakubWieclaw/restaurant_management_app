package com.example.restaurantmanagementapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.ui.theme.RestaurantManagementAppTheme

@Composable
fun MealList(meals: List<Meal>){
    LazyColumn{
        items(meals){meal ->
            MealCard(meal = meal)
        }
    }

}

@Preview(
    showBackground = true
)
@Composable
fun MealListPreview(){
    RestaurantManagementAppTheme {
        MealList(TestData.mealListSample)
    }
}

@Composable
fun MealCard(meal: Meal){
    Column(modifier = Modifier.padding(all = 8.dp)){

        val shape = RoundedCornerShape(30.dp)
        Box(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .background(White,shape=shape),
            contentAlignment = Alignment.BottomStart){
            Image(
                painter = painterResource(id = R.drawable.test_meal_picture_1),
                contentDescription = "Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
            )
            Text(meal.name, color = White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()){
            Text("rating")
            Text("details")
            Text("add to order")
        }
    }
}

//@Preview
//@Composable
//fun MealCardPreview(modifier: Modifier = Modifier){
//    RestaurantManagementAppTheme {
//        MealCard()
//    }
//}

