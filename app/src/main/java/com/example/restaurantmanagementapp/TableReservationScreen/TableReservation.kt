package com.example.restaurantmanagementapp.TableReservationScreen

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.TestData
import com.example.restaurantmanagementapp.UserPanelScreen.ExposedDropdownMenuBox
import com.example.restaurantmanagementapp.classes.Table
import java.util.Calendar

@Composable
fun TableReservation(tables: List<Table>) {
    var selectedDate by remember { mutableStateOf("1990/1/1") }
    var selectedTime1 by remember { mutableStateOf("00:00") }
    var selectedTime2 by remember { mutableStateOf("23:59") }
    var selectedSits by remember { mutableStateOf("0") }
    var tablenr by remember { mutableIntStateOf(-1) }

    val filteredTables = tables.filter {
        it.sits >= selectedSits.toInt() &&
                compareTime(selectedTime1, it.startHour) >= 0 &&
                compareTime(selectedTime2, it.endHour) <= 0
    }

    val choosenTable = tables.find { table -> table.nr == tablenr }

    Scaffold(
        bottomBar = { TableBottomBar(table = choosenTable, onDelete = {tablenr = -1}) }
    ) {innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            FilterOptions(
                onDateChange = { date -> selectedDate = date },
                onTime1Change = { time1 -> selectedTime1 = time1 },
                onTime2Change = { time2 -> selectedTime2 = time2 },
                onSitsChange = { sits -> selectedSits = sits }
            )
              //Debug
//            Text(selectedDate)
//            Text(selectedTime1)
//            Text(selectedTime2)
//            Text(selectedSits)

            LazyColumn(modifier = Modifier
                .padding(10.dp)
                .weight(1f)) {
                items(filteredTables) { table ->
                    if (table.nr == tablenr)
                        TableCart(table, isChoosen = true, onChoose = { nr -> tablenr = nr })
                    else
                        TableCart(table, isChoosen = false, onChoose = { nr -> tablenr = nr })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
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
        Text("Filtruj:")
        Row(){
            Column(modifier = Modifier.padding(start=10.dp)){
                Text("Dzień")
                OutlinedButton(modifier = Modifier
                    .padding(start=8.dp)
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
            }
            Column(modifier = Modifier.padding(start=10.dp)) {
                Text("Liczba osób")
                ExposedDropdownMenuBox(
                    itemList = sitsNum,
                    onValueChange = { selected -> onSitsChange(selected) },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(50.dp)
                        .width(80.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.padding(start=10.dp), horizontalArrangement = Arrangement.Center){
                Text("Od:", modifier = Modifier.align(Alignment.CenterVertically))
                OutlinedButton(modifier = Modifier
                    .padding(start = 6.dp)
                    .height(50.dp)
                    .width(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        val timePickerDialog = TimePickerDialog(
                            context,
                            { _, selectedHour, selectedMinute ->
                                // Zaokrąglenie minut do 0 lub 30
                                val roundedMinute = if (selectedMinute >= 30) 30 else 0
                                selectedTime =
                                    "$selectedHour:${if (roundedMinute < 10) "0" else ""}$roundedMinute"
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
                Text("Do:",modifier = Modifier.padding(start=10.dp).align(Alignment.CenterVertically))
                OutlinedButton(modifier = Modifier
                    .padding(start = 6.dp)
                    .height(50.dp)
                    .width(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        val timePickerDialog = TimePickerDialog(
                            context,
                            { _, selectedHour, selectedMinute ->
                                // Zaokrąglenie minut do 0 lub 30
                                val roundedMinute = if (selectedMinute >= 30) 30 else 0
                                selectedTime2 =
                                    "$selectedHour:${if (roundedMinute < 10) "0" else ""}$roundedMinute"
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

@Composable
fun TableBottomBar(table:Table?, onDelete:()->Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
        .background(color = Color.White)
        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
        ){
        Text("Wybrany stolik:")
        Spacer(modifier = Modifier.height(10.dp))
        if(table!=null){
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)){
                TableCart(table = table, isChoosen = false, onChoose = {}, modifier = Modifier.weight(0.8f))
                Button(
                    onClick = {onDelete()},
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(start = 8.dp)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(10.dp)

                ){
                    Icon(imageVector = Icons.Default.Clear,contentDescription = null)
                }
            }
        }else{
            Text(text = "Nie wybrano stolika",modifier = Modifier.height(40.dp).fillMaxWidth(), textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick={},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            enabled = table!=null){
            Text("Zamów stolik")
        }

    }
}
private fun compareTime(time1:String,time2:String):Int{
    if (time1 == time2) return 0
    val time1parts = time1.split(":")
    val time2parts = time2.split(":")
    return if(time1parts[0].toInt()>time2parts[0].toInt()) 1 else if (time1parts[0].toInt()==time2parts[0].toInt()&&time1parts[1].toInt()>time2parts[1].toInt()) 1 else -1
}

