package com.example.restaurantmanagementapp.TableReservationScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.apithings.schemasclasses.LocalTime
import com.example.restaurantmanagementapp.classes.Table


//@Preview
//@Composable
//fun TableCartPreview(){
//    TableCart()
//}
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
//        Image(
//            painter = if(selectedSits.toInt()==2) painterResource(id = R.drawable.table2) else if(selectedSits.toInt()==4) painterResource(id = R.drawable.table4) else painterResource(id = R.drawable.table6),
//            contentDescription = null,
//            modifier = Modifier
//                .padding(10.dp)
//                .width(100.dp)
//                .height(80.dp)
//                .clip(RoundedCornerShape(8.dp)),
//            contentScale = ContentScale.Crop
//        )
            Text(text=time)
    }
}

