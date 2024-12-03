package com.example.restaurantmanagementapp.TableReservationScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.UserPanelScreen.OrderCard
import com.example.restaurantmanagementapp.UserPanelScreen.formatDateTime
import com.example.restaurantmanagementapp.UserPanelScreen.getOrderStatus
import com.example.restaurantmanagementapp.UserPanelScreen.getOrderType
import com.example.restaurantmanagementapp.UserPanelScreen.getStatusColor
import com.example.restaurantmanagementapp.apithings.schemasclasses.TableReservation
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.MealsViewModel

@Composable
fun TableReservationCard(tableReservation: TableReservation, mealsViewModel: MealsViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ){
        Row(modifier = Modifier.fillMaxWidth()){
            Image(
                painter =
                if(tableReservation.people<3) {
                    painterResource(R.drawable.table2) }else{
                    if(tableReservation.people<5){
                        painterResource(R.drawable.table4)
                    }else{
                        painterResource(R.drawable.table6)
                    }
                }, contentDescription = null, alignment = Alignment.CenterStart)
            Column(){
                Text(text = stringResource(id = R.string.day) +": " +tableReservation.day)
                Text(text = stringResource(id = R.string.number_of_people) +": ${tableReservation.people}")
                Text(text= stringResource(id = R.string.hour) +": ${tableReservation.startTime.dropLast(3)} - ${tableReservation.endTime.dropLast(3)}")
            }
        }
        if(!tableReservation.orders.isNullOrEmpty()){
            tableReservation.orders.forEachIndexed{index, order ->
                Text(text = stringResource(id = R.string.order) + "#${index+1}")
                OrderCard(order = order, mealsViewModel = mealsViewModel, toTableRes = true)
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
            Text(text = stringResource(id = R.string.totalSum) +": ${tableReservation.orders.sumOf{it.orderPrice}} " + stringResource(id = R.string.currency))
        }
    }

}
