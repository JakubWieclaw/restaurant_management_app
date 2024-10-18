package com.example.restaurantmanagementapp.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

data class Meal(
    override val id: Int,
    override val name: String,
    override var price: Double,
    override val photographUrl: String,
    override val ingredients: List<String>,
    override val weightOrVolume: Int,
    override val unitType: String,
    override val categoryId: Int,
    override val allergens: List<String>,
    override val calories: Int,
    override val unitTypeMandatory: Boolean,
    val quantity2: Int = 1,
    var avgRating: Double = 0.0,
    var opinions: List<Opinion> = emptyList(),
    var removedIngredients: SnapshotStateList<String> = mutableStateListOf(),
) : MealServer(
    id = id,
    name = name,
    price = price,
    photographUrl = photographUrl,
    ingredients = ingredients,
    weightOrVolume = weightOrVolume,
    unitType = unitType,
    categoryId = categoryId,
    allergens = allergens,
    calories = calories,
    unitTypeMandatory = unitTypeMandatory
) {
    var quantity by mutableIntStateOf(quantity2)

}