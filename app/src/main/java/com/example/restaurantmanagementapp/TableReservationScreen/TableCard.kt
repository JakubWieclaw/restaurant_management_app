package com.example.restaurantmanagementapp.TableReservationScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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


@Preview
@Composable
fun TableCartPreview(){
    TableCart()
}
@Composable
fun TableCart(){
    Row(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Gray, RoundedCornerShape(8.dp))
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        Image(
            painter = painterResource(id = R.drawable.table6),
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
                .width(100.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier
            .fillMaxHeight()
            .padding(10.dp),
            verticalArrangement =  Arrangement.SpaceBetween){
            Text("Table nr: 10", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("6 sits", fontSize = 16.sp)
            Text("21.03.2024 18:00-19:00", fontSize = 14.sp)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
            Button(
                onClick = {},
                modifier = Modifier
                    .padding(10.dp)
                    .height(60.dp)
                    .width(80.dp),
                shape = RoundedCornerShape(8.dp)
                ){
                Icon(imageVector = Icons.Default.Check,contentDescription = null,Modifier.size(50.dp))
            }
        }

    }
}

