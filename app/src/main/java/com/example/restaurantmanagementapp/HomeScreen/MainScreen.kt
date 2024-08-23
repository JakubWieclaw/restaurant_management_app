package com.example.restaurantmanagementapp.HomeScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.restaurantmanagementapp.TestData
import com.example.restaurantmanagementapp.classes.OrderViewModel
import com.example.restaurantmanagementapp.ui.theme.RestaurantManagementAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurantManagementAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TestMainScreen()
                }
            }
        }
    }
}

@Composable
fun TestMainScreen(){
    val navController = rememberNavController()
    val meals = TestData.mealListSample
    val categories = TestData.categories
    val images = TestData.imagesList
    val orderViewModel: OrderViewModel = viewModel()

    SetupNavGraph(
        navController = navController,
        images = images,
        meals = meals,
        categories = categories,
        orderViewModel = orderViewModel
    )
}