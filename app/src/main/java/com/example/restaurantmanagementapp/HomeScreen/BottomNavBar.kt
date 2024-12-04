package com.example.restaurantmanagementapp.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderViewModel

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    data object GeneralInfo : BottomNavItem("Aktualności", Icons.Default.Info, "restaurantinfo")
    data object DishSearch : BottomNavItem("Szukaj", Icons.Default.Search, "meallist")
    data object TableReservation : BottomNavItem("Rezerwacja", Icons.Default.Star, "tablereservation")
    data object UserProfile : BottomNavItem("Profil", Icons.Default.Person, "userpanel")
    data object Cart: BottomNavItem("Koszyk", Icons.Default.ShoppingCart, "cart")
}
@Composable
fun BottomNavigationBar(navController: NavController, orderViewModel: OrderViewModel, authViewModel: AuthViewModel,modifier:Modifier) {
    val items = listOf(
        BottomNavItem.GeneralInfo,
        BottomNavItem.DishSearch,
        BottomNavItem.TableReservation,
        BottomNavItem.UserProfile,
        BottomNavItem.Cart
    )

    NavigationBar(modifier = Modifier.then(modifier)) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    if(item == BottomNavItem.Cart)
                    {
                        CartIconWithBadge(orderViewModel.getSize(),modifier = Modifier.size(32.dp))
                    }else{
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                label = {
                    Text(text = item.title, style = Typography.bodySmall)
                },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        if(item == BottomNavItem.UserProfile){
                            if(authViewModel.isLogged){
                                navigateToScreen("userpanel",navController)
                            }else{
                                navigateToScreen("loginscreen",navController)
                            }
                        }else{
                            navigateToScreen(item.route,navController)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CartIconWithBadge(cartItemCount: Int, modifier:Modifier) {
    Box(contentAlignment = Alignment.TopEnd) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Cart",
            modifier = Modifier.then(modifier) // Rozmiar ikony koszyka
        )

        if (cartItemCount > 0) {
            Text(
                text = cartItemCount.toString(),
                color = Color.White,
                fontSize = 13.sp,
                modifier = Modifier
                    .offset(x = 12.dp, y = (-10).dp) // Przesunięcie liczby nad ikoną koszyka
                    .background(Color.Red, shape = CircleShape) // Tło w formie okręgu
                    .padding(4.dp) // Wewnętrzne odstępy
            )
        }
    }
}