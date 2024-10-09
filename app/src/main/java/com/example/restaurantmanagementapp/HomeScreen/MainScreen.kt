package com.example.restaurantmanagementapp.HomeScreen

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restaurantmanagementapp.TestData
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.classes.AuthViewModel
import com.example.restaurantmanagementapp.classes.CategoriesViewModel
import com.example.restaurantmanagementapp.classes.FavMealsViewModel
import com.example.restaurantmanagementapp.classes.MealsViewModel
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
    val mealsViewModel: MealsViewModel = viewModel()
    val images = TestData.imagesList
    val orderViewModel: OrderViewModel = viewModel()
    val authViewModel: AuthViewModel= viewModel()
    val favMealsViewModel: FavMealsViewModel = viewModel()
    val categoriesViewModel: CategoriesViewModel = viewModel()
    val tables = TestData.tablesList

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
                tables = tables,
                categoriesViewModel = categoriesViewModel
            )
        }
    }
}

