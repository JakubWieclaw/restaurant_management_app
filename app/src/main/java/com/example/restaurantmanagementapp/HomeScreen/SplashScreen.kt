package com.example.restaurantmanagementapp.HomeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.TestData
import com.example.restaurantmanagementapp.classes.AuthViewModel
import com.example.restaurantmanagementapp.classes.CategoriesViewModel
import com.example.restaurantmanagementapp.classes.FavMealsViewModel
import com.example.restaurantmanagementapp.classes.OrderViewModel

@Composable
fun SplashScreen(categoriesViewModel: CategoriesViewModel, navController: NavController) {
    val totalItems = 1
    var completed by remember { mutableIntStateOf(0) }

    categoriesViewModel.fetchCategories(onComplete={completed++})
    val progress = completed.toFloat() / totalItems.toFloat()

    if (completed >= totalItems) {
        navigateToScreen("restaurantinfo", navController)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.test_meal_picture_1),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Pobrano: ${(progress * 100).toInt()}%")
        }
    }
}
