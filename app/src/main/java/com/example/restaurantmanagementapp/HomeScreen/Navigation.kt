package com.example.restaurantmanagementapp.HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.restaurantmanagementapp.LoginScreen.LoginScreen
import com.example.restaurantmanagementapp.CartScreen.CartScreen
import com.example.restaurantmanagementapp.CartScreen.PaymentConfirm
import com.example.restaurantmanagementapp.FavouriteMealsScreen.FavouriteMeals
import com.example.restaurantmanagementapp.LoginScreen.PasswordResetScreen
import com.example.restaurantmanagementapp.MealListScreen.MealList
import com.example.restaurantmanagementapp.MealDetailsScreen.MealScreen
import com.example.restaurantmanagementapp.RestaurantInfoScreen.RestaurantInfo
import com.example.restaurantmanagementapp.TableReservationScreen.TableReservation
import com.example.restaurantmanagementapp.UserPanelScreen.OrderHistory
import com.example.restaurantmanagementapp.UserPanelScreen.SettingsScreen
import com.example.restaurantmanagementapp.UserPanelScreen.SuggestionForm
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel
import com.example.restaurantmanagementapp.viewmodels.CategoriesViewModel
import com.example.restaurantmanagementapp.viewmodels.CouponsViewModel
import com.example.restaurantmanagementapp.viewmodels.FavMealsViewModel
import com.example.restaurantmanagementapp.viewmodels.MealsViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderViewModel
import com.example.restaurantmanagementapp.classes.Table
import com.example.restaurantmanagementapp.viewmodels.HoursViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderHistoryViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    images: List<Int>,
    mealsViewModel: MealsViewModel,
    orderViewModel: OrderViewModel,
    authViewModel: AuthViewModel,
    favMealsViewModel: FavMealsViewModel,
    categoriesViewModel: CategoriesViewModel,
    couponsViewModel: CouponsViewModel,
    orderHistoryViewModel: OrderHistoryViewModel,
    hoursViewModel: HoursViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = "splashscreen"
    ) {
        composable("restaurantinfo") {
            RestaurantInfo(images = images, couponsViewModel = couponsViewModel,navController = navController)
        }
        composable("loginscreen") {
            LoginScreen(navController = navController ,authViewModel = authViewModel, couponsViewModel = couponsViewModel)
        }
        composable("userpanel"){
            SettingsScreen(navController = navController, orderHistoryViewModel = orderHistoryViewModel, authViewModel = authViewModel)
        }
        composable("orderhistory"){
            OrderHistory(orderHistoryViewModel = orderHistoryViewModel, mealsViewModel = mealsViewModel)
        }
        composable("tablereservation"){
            TableReservation(hoursViewModel = hoursViewModel, authViewModel = authViewModel, mealsViewModel = mealsViewModel)
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
            CartScreen(orderViewModel = orderViewModel,couponsViewModel = couponsViewModel,authViewModel = authViewModel,hoursViewModel=hoursViewModel,navController=navController)
        }


        composable("splashscreen"){
            SplashScreen(categoriesViewModel = categoriesViewModel, mealsViewModel = mealsViewModel, couponsViewModel = couponsViewModel, authViewModel = authViewModel,favMealsViewModel = favMealsViewModel, navController = navController)
        }

        composable("suggestionform"){
            SuggestionForm(authViewModel=authViewModel,navController=navController)
        }
        composable("passwordreset"){
            PasswordResetScreen(navController)
        }
        composable("paymentconfirm"){
            PaymentConfirm(orderViewModel=orderViewModel,authViewModel=authViewModel,navController = navController)
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
