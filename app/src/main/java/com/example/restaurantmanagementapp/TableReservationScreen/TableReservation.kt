package com.example.restaurantmanagementapp.TableReservationScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.UserPanelScreen.ExposedDropdownMenuBox
import com.example.restaurantmanagementapp.apithings.schemasclasses.Table
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel
import com.example.restaurantmanagementapp.viewmodels.HoursViewModel
import com.example.restaurantmanagementapp.viewmodels.MealsViewModel
import java.util.Calendar

@Composable
fun TableReservation(hoursViewModel: HoursViewModel, authViewModel: AuthViewModel,mealsViewModel: MealsViewModel) {

    var selectedDate by remember { mutableStateOf("1990/1/1") }
    var selectedSits by remember { mutableStateOf("1") }
    var choosenCard by remember { mutableStateOf("-1") }
    var reservationFetched by remember { mutableStateOf(false) }

    LaunchedEffect(!reservationFetched) {
        if(authViewModel.isLogged){
            reservationFetched = hoursViewModel.getReservations(authViewModel.customerData!!.customerId,authViewModel.customerData!!.token)
        }
    }

    val colScrollState = rememberScrollState()

    Column(
        modifier = if(hoursViewModel.tableReservations.isNotEmpty()){
        Modifier
            .verticalScroll(colScrollState)
            .fillMaxSize().padding(12.dp) } else {
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    }){

            Text(text = stringResource(id = R.string.reservation), style = Typography.titleLarge )
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 12.dp)
            )
            FilterOptions(
                onDateChange = { date -> selectedDate = date;hoursViewModel.fetchAvailableHours(selectedDate,selectedSits,authViewModel.customerData!!.token) },
                onSitsChange = { sits -> selectedSits = sits;hoursViewModel.fetchAvailableHours(selectedDate,selectedSits,authViewModel.customerData!!.token) },
                onSearchClick = { hoursViewModel.fetchAvailableHours(selectedDate,selectedSits,authViewModel.customerData!!.token)}
            )
            if(hoursViewModel.tableReservations.isNotEmpty()&&reservationFetched){
                TableReservationCard(hoursViewModel.tableReservations[0], mealsViewModel = mealsViewModel)
            }else {
                if (hoursViewModel.tableHours != null) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        items(hoursViewModel.tableHours!!.chunked(2)) { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Dla każdego elementu w parze tworzymy kolumnę
                                rowItems.forEachIndexed { index, localTime ->
                                    val actualIndex = hoursViewModel.tableHours!!.indexOf(localTime)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    TableCart(
                                        time = localTime.dropLast(3),
                                        selectedSits = selectedSits,
                                        index = actualIndex,
                                        isChoosen = choosenCard == actualIndex.toString(),
                                        onChoose = { nr -> choosenCard = nr },
                                        modifier = Modifier.weight(0.5f)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                } else {
                    Text("Pusta lista", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
                }
            }
            if(hoursViewModel.tableReservations.isNotEmpty()){
                Button(
                    onClick = {hoursViewModel.cancelReservation(customerToken = authViewModel.customerData!!.token)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(Color.Red)){
                    Text("Anuluj rezerwację", color = Color.White)
                }
            }else {
                Button(
                    onClick = {
                        if (hoursViewModel.localTimes != null && authViewModel.customerData != null && choosenCard.toInt()>-1) {
                            hoursViewModel.makeReservation(
                                day = selectedDate,
                                startTime = hoursViewModel.localTimes!![choosenCard.toInt()],
                                numberOfPeople = selectedSits.toInt(),
                                customerId = authViewModel.customerData!!.customerId,
                                customerToken = authViewModel.customerData!!.token
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    enabled = hoursViewModel.localTimes != null && authViewModel.customerData != null && choosenCard.toInt()>-1
                ) {
                    Text("Zarezerwuj")
                }
            }
        }
    }

@Composable
fun FilterOptions(
    onDateChange: (String) -> Unit,
    onSitsChange: (String) -> Unit,
    onSearchClick: () -> Unit
){
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    var selectedDate by remember { mutableStateOf("$year-${String.format("%02d",month + 1)}-${String.format("%02d",day)}") }
    val context = LocalContext.current

    val sitsNum = listOf("1","2","3","4","5","6")

    LaunchedEffect(selectedDate) {
        onDateChange(selectedDate)
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text("Dzień")
                Button(modifier = Modifier
                    .height(50.dp)
                    .width(150.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        val datePickerDialog = android.app.DatePickerDialog(
                            context,
                            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                                selectedDate = "$selectedYear-${String.format("%02d",selectedMonth + 1)}-${
                                    String.format(
                                        "%02d",
                                        selectedDayOfMonth
                                    )
                                }"
                            },
                            year,
                            month,
                            day
                        )
                        datePickerDialog.show()
                    }
                ) {
                    Text(selectedDate)
                }
}
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text("Liczba osób")
                    ExposedDropdownMenuBox(
                        itemList = sitsNum,
                        onValueChange = { selected -> onSitsChange(selected) },
                        modifier = Modifier
                            .height(50.dp)
                            .width(80.dp)
                    )
                }

        }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)){
                Button(onClick = onSearchClick,modifier= Modifier.fillMaxWidth()){
                    Text("Szukaj ",style = Typography.labelLarge)
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
        }
    }
}