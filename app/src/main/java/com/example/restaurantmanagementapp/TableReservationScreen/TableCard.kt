package com.example.restaurantmanagementapp.TableReservationScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TableCart(time: String,selectedSits:String,index:Int, isChoosen: Boolean = false, onChoose:(String) -> Unit, modifier:Modifier = Modifier){
    Row(modifier = Modifier
        .background(color = if(isChoosen) Color.Red else Color.Green, RoundedCornerShape(8.dp))
        .height(100.dp)
        .clickable { onChoose(if(isChoosen) "-1" else index.toString()) }
        .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ){
            Text(text=time)
    }
}

