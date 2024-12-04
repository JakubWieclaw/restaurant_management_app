package com.example.restaurantmanagementapp.UserPanelScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.HomeScreen.CustomBackground
import com.example.restaurantmanagementapp.viewmodels.MealsViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderHistoryViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun OrderHistory(orderHistoryViewModel: OrderHistoryViewModel, mealsViewModel: MealsViewModel) {

    var selectedType by remember { mutableStateOf<String?>(null)}
    var selectedStatus by remember { mutableStateOf<String?>(null)}
    var sortOrderAsc by remember { mutableStateOf(true) }

    val orderTypes = listOf("NA_MIEJSCU", "DOSTAWA")
    val orderStatuses = listOf("OCZEKUJĄCE", "W_TRAKCIE_REALIZACJI", "GOTOWE", "W_DOSTARCZENIU", "DOSTARCZONE", "ODRZUCONE")


    val filteredOrders = orderHistoryViewModel.orderHistory
        .filter { order ->
            (selectedType == null || order.type == selectedType) &&
                    (selectedStatus==null || order.status == selectedStatus)
        }
        .sortedBy { order ->
            order.dateTime
        }

    val filteredOrders2 = if (!sortOrderAsc) filteredOrders.reversed() else filteredOrders

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            // Filtr typów
            DropdownFilter(
                label = "Typ",
                options = orderTypes,
                selectedOption = selectedType,
                onOptionSelected = { selectedType = it },
                statusOrType = "type"
            )

            // Filtr statusów
            DropdownFilter(
                label = "Status",
                options = orderStatuses,
                selectedOption = selectedStatus,
                onOptionSelected = { selectedStatus = it },
                statusOrType = "status"
            )
        }


        // Przycisk sortowania
        SortButton(
            ascending = sortOrderAsc,
            onToggleSortOrder = { sortOrderAsc = !sortOrderAsc }
        )

        // Lista zamówień
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {
            itemsIndexed(filteredOrders2) { index, order ->
                OrderCard(order = order, mealsViewModel = mealsViewModel)
                Spacer(modifier = Modifier.padding(bottom = 12.dp))
            }
        }
    }
}


@Composable
fun DropdownFilter(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit,
    statusOrType: String
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label)
        Box {
            Button(onClick = { expanded = true }) {
                Text(text = if(selectedOption!=null){
                    if(statusOrType=="status"){
                        getOrderStatus(selectedOption)
                    } else {
                        getOrderType(selectedOption)}
                } else {"Wszystkie"})
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = {
                    onOptionSelected(null)
                    expanded = false
                }, text = { Text("Wszystkie")})
                options.forEach { option ->
                    DropdownMenuItem(onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },text = {if(statusOrType=="status") Text(getOrderStatus(option)) else Text(
                        getOrderType(option))})
                }
            }
        }
    }
}

@Composable
fun SortButton(
    ascending: Boolean,
    onToggleSortOrder: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Sortuj wg daty: ")
        Button(onClick = onToggleSortOrder) {
            Text(if (ascending) "Rosnąco" else "Malejąco")
        }
    }
}
