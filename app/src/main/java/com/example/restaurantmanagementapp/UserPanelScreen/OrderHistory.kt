package com.example.restaurantmanagementapp.UserPanelScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.HomeScreen.CustomBackground
import com.example.restaurantmanagementapp.viewmodels.MealsViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderHistoryViewModel

@Composable
fun OrderHistory(orderHistoryViewModel: OrderHistoryViewModel, mealsViewModel: MealsViewModel) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        itemsIndexed(orderHistoryViewModel.orderHistory){ index, order ->
            OrderCard(order = order, mealsViewModel = mealsViewModel)
            Spacer(modifier = Modifier.padding(bottom=12.dp))
        }
    }
}
