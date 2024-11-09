package com.example.restaurantmanagementapp.HomeScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.restaurantmanagementapp.TestData
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel
import com.example.restaurantmanagementapp.viewmodels.CategoriesViewModel
import com.example.restaurantmanagementapp.viewmodels.CouponsViewModel
import com.example.restaurantmanagementapp.viewmodels.FavMealsViewModel
import com.example.restaurantmanagementapp.viewmodels.MealsViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderViewModel
import com.example.restaurantmanagementapp.ui.theme.RestaurantManagementAppTheme
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.HoursViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderHistoryViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurantManagementAppTheme {
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
    val mealsViewModel: MealsViewModel = viewModel()
    val images = TestData.imagesList
    val orderViewModel: OrderViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val favMealsViewModel: FavMealsViewModel = viewModel()
    val categoriesViewModel: CategoriesViewModel = viewModel()
    val couponsViewModel: CouponsViewModel = viewModel()
    val orderHistoryViewModel: OrderHistoryViewModel = viewModel()
    val hoursViewModel: HoursViewModel = viewModel()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController,orderViewModel, authViewModel)
        }
    ){innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)){
            SetupNavGraph(
                navController = navController,
                images = images,
                mealsViewModel = mealsViewModel,
                orderViewModel = orderViewModel,
                authViewModel = authViewModel,
                favMealsViewModel = favMealsViewModel,
                categoriesViewModel = categoriesViewModel,
                couponsViewModel = couponsViewModel,
                orderHistoryViewModel =orderHistoryViewModel,
                hoursViewModel = hoursViewModel
            )
        }
    }
}

