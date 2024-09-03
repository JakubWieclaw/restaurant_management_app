package com.example.restaurantmanagementapp.TableReservationScreen

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.UserPanelScreen.ExposedDropdownMenuBox
import com.example.restaurantmanagementapp.classes.Table
import java.util.Calendar

@Composable
fun TableReservation(tables:List<Table>){
    var selectedDate by remember{ mutableStateOf("")}
    var selectedTime1 by remember{ mutableStateOf("")}
    var selectedTime2 by remember{ mutableStateOf("")}
    var selectedSits by remember{ mutableStateOf("")}

    Column(modifier = Modifier
        .fillMaxSize()
    ){
        FilterOptions(
            onDateChange = {date -> selectedDate = date},
            onTime1Change = {time1 -> selectedTime1 = time1},
            onTime2Change = {time2 -> selectedTime2 = time2},
            onSitsChange = {sits -> selectedSits = sits},
        )
//        val filteredTables = tables.filter{table ->
//
//        }
        Text(selectedDate)
        Text(selectedTime1)
        Text(selectedTime2)
        Text(selectedSits)
        LazyColumn {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterOptions(
    onDateChange: (String) -> Unit,
    onTime1Change: (String) -> Unit,
    onTime2Change: (String) -> Unit,
    onSitsChange: (String) -> Unit,
){
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    var selectedDate by remember { mutableStateOf("$year/${month + 1}/$day") }
    val context = LocalContext.current


    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    var minute = calendar.get(Calendar.MINUTE)
    minute = if (minute >= 30) 30 else 0
    var selectedTime by remember { mutableStateOf("$hour:${minute}${if (minute<10) "0" else ""}") }
    //val context = LocalContext.current
    //val calendar = Calendar.getInstance()

    //selectedTime =

    val hour2 = calendar.get(Calendar.HOUR_OF_DAY)
    var minute2 = calendar.get(Calendar.MINUTE)
    minute2 = if (minute2 >= 30) 30 else 0
    var selectedTime2 by remember { mutableStateOf("$hour:${minute2}${if (minute2<10) "0" else ""}") }
    //val context = LocalContext.current
    //val calendar = Calendar.getInstance()


    val sitsNum = listOf("1","2","3","4","5","6")
    var clientsNumber by remember{ mutableStateOf("")}

    LaunchedEffect(selectedDate) {
        onDateChange(selectedDate)
    }
    LaunchedEffect(selectedTime) {
        onTime1Change(selectedTime)
    }
    LaunchedEffect(selectedTime2) {
        onTime2Change(selectedTime2)
    }

    Column(){
        Row(){
            OutlinedButton(modifier = Modifier
                .padding(10.dp)
                .height(50.dp)
                .width(140.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    val datePickerDialog = android.app.DatePickerDialog(
                        context,
                        { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                            selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
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
            ExposedDropdownMenuBox(itemList = sitsNum, onValueChange = { selected -> onSitsChange(selected)}, modifier = Modifier
                .padding(10.dp)
                .height(50.dp)
                .width(80.dp))

        }
        Row(){
            OutlinedButton(modifier = Modifier
                .padding(10.dp)
                .height(50.dp)
                .width(100.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    val timePickerDialog = TimePickerDialog(
                        context,
                        { _, selectedHour, selectedMinute ->
                            // Zaokrąglenie minut do 0 lub 30
                            val roundedMinute = if (selectedMinute >= 30) 30 else 0
                            selectedTime = "$selectedHour:${if (roundedMinute < 10) "0" else ""}$roundedMinute"
                        },
                        hour,
                        minute,
                        true // Ustawienie na true dla formatu 24-godzinnego
                    )
                    timePickerDialog.show()
                }
            ) {
                Text(selectedTime)
            }
            OutlinedButton(modifier = Modifier
                .padding(10.dp)
                .height(50.dp)
                .width(100.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    val timePickerDialog = TimePickerDialog(
                        context,
                        { _, selectedHour, selectedMinute ->
                            // Zaokrąglenie minut do 0 lub 30
                            val roundedMinute = if (selectedMinute >= 30) 30 else 0
                            selectedTime2 = "$selectedHour:${if (roundedMinute < 10) "0" else ""}$roundedMinute"
                        },
                        hour2,
                        minute2,
                        true
                    )
                    timePickerDialog.show()
                }
            ) {
                Text(selectedTime2)
            }
        }
    }
}

