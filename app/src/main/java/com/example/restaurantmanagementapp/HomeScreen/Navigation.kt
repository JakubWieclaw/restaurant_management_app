package com.example.restaurantmanagementapp.HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.restaurantmanagementapp.LoginScreen.LoginScreen
import com.example.restaurantmanagementapp.CartScreen.CartScreen
import com.example.restaurantmanagementapp.FavouriteMealsScreen.FavouriteMeals
import com.example.restaurantmanagementapp.MealListScreen.MealList
import com.example.restaurantmanagementapp.MealDetailsScreen.MealScreen
import com.example.restaurantmanagementapp.RestaurantInfoScreen.RestaurantInfo
import com.example.restaurantmanagementapp.TableReservationScreen.TableReservation
import com.example.restaurantmanagementapp.UserPanelScreen.SettingsScreen
import com.example.restaurantmanagementapp.classes.AuthViewModel
import com.example.restaurantmanagementapp.classes.CategoriesViewModel
import com.example.restaurantmanagementapp.classes.FavMealsViewModel
import com.example.restaurantmanagementapp.classes.Meal
import com.example.restaurantmanagementapp.classes.MealsViewModel
import com.example.restaurantmanagementapp.classes.OrderViewModel
import com.example.restaurantmanagementapp.classes.Table

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    images: List<Int>,
    mealsViewModel: MealsViewModel,
    orderViewModel: OrderViewModel,
    authViewModel: AuthViewModel,
    favMealsViewModel: FavMealsViewModel,
    categoriesViewModel: CategoriesViewModel,
    tables: List<Table>
) {
    NavHost(
        navController = navController,
        startDestination = "splashscreen"
    ) {
        composable("restaurantinfo") {
            RestaurantInfo(images = images, navController = navController)
        }
        composable("loginscreen") {
            LoginScreen(navController = navController ,authViewModel = authViewModel)
        }
        composable("userpanel"){
            SettingsScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("tablereservation"){
            TableReservation(tables = tables)
        }
        composable("favourites"){
            FavouriteMeals(orderViewModel = orderViewModel,favMealsViewModel = favMealsViewModel)
        }


        composable("meallist") {
            MealList(meals = mealsViewModel.meals,navController = navController, orderViewModel = orderViewModel,authViewModel = authViewModel, categoriesViewModel = categoriesViewModel)
        }
        composable("meal/{mealId}") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId")
            val meal = mealsViewModel.meals.find { it.id.toString() == mealId }!!
            MealScreen(meal = meal, navController = navController, orderViewModel = orderViewModel, authViewModel = authViewModel, favMealsViewModel = favMealsViewModel)
        }
        composable("cart") {
            CartScreen(orderViewModel = orderViewModel,authViewModel = authViewModel)
        }


        composable("splashscreen"){
            SplashScreen(categoriesViewModel = categoriesViewModel, mealsViewModel = mealsViewModel,navController = navController)
        }
    }
}

fun navigateToScreen(route: String, navController: NavController) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
