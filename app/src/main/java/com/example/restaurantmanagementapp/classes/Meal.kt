package com.example.restaurantmanagementapp.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Meal(
    val id: Int,
    val name: String,
    val price: Double,
    val categoryID: Int,
    val opinionID: Int,
    val ingredientID: Int,
    val picture: String,
    val quantity2:Int = 1,
) {
    var quantity by mutableIntStateOf(quantity2)
}