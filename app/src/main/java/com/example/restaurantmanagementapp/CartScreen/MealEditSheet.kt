package com.example.restaurantmanagementapp.CartScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealEditSheet(orderViewModel: OrderViewModel, index:Int?, onDismissRequest:()->Unit, sheetState: SheetState){
    if(index!=null && index < orderViewModel.orderItems.size) {
        ModalBottomSheet(
            onDismissRequest = {onDismissRequest()},
            sheetState = sheetState
        ) {
            val tmeal = orderViewModel.orderItems[index]
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth()){
                    Text(
                        text = stringResource(id = R.string.mealeditheader) + " " + tmeal.name,
                        style = Typography.titleSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    IconButton(onClick={onDismissRequest()}){
                        Icon(imageVector= Icons.Default.Close, contentDescription = null)
                    }
                }

                tmeal.removableIngredList.forEach { ingredient ->
                    val isRemoved =
                        tmeal.removedIngredients.find { item -> item == ingredient } == ingredient
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "- $ingredient",
                            textDecoration = if (isRemoved) TextDecoration.LineThrough else TextDecoration.None,
                            style = Typography.labelMedium
                        )
                        Row() {
                            IconButton(
                                onClick = { orderViewModel.addIngredient(index, ingredient) },
                                enabled = isRemoved,
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = if (isRemoved) Color.Green else Color.Gray,
                                        shape = RoundedCornerShape(50)
                                    )
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            IconButton(
                                onClick = { orderViewModel.removeIngredient(index, ingredient) },
                                enabled = !isRemoved,
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = if (!isRemoved) Color.Red else Color.Gray,
                                        shape = RoundedCornerShape(50)
                                    )
                            ) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }else{
        println("index = null")
    }
}

