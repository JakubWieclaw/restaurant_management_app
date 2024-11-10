package com.example.restaurantmanagementapp.FavouriteMealsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.HomeScreen.CustomBackground
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.FavMealsViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderViewModel

@Composable
fun FavouriteMeals(orderViewModel: OrderViewModel, favMealsViewModel: FavMealsViewModel){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)){

        Text(text= stringResource(id = R.string.favourite) , style = Typography.titleLarge)

        Divider(
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(favMealsViewModel.favItems.size) {index ->
                FavouriteMealCard(
                    meal = favMealsViewModel.favItems[index],
                    onAddToCart = {meal -> orderViewModel.addToOrder(meal)},
                    onDelete = {meal -> favMealsViewModel.removeFromFav(meal)}
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }

}