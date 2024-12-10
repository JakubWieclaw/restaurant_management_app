package com.example.restaurantmanagementapp.HomeScreen

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.restaurantmanagementapp.R
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
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
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
    val images = listOf("image1.jpg","image2.jpg","image3.jpg","image4.jpg","lokalizacja.jpg")
    val orderViewModel: OrderViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val favMealsViewModel: FavMealsViewModel = viewModel()
    val categoriesViewModel: CategoriesViewModel = viewModel()
    val couponsViewModel: CouponsViewModel = viewModel()
    val orderHistoryViewModel: OrderHistoryViewModel = viewModel()
    val hoursViewModel: HoursViewModel = viewModel()
    Scaffold(
        bottomBar = {BottomNavigationBar(navController,orderViewModel, authViewModel,modifier=Modifier)}
    ) {innerpadding ->
        Surface(modifier = Modifier.fillMaxWidth().padding(innerpadding)){
            CustomBackground()
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

@Composable
fun CustomBackground(){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)){
        Image(
            painter = painterResource(id = R.drawable.wood_background2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f)
        )
    }
}

