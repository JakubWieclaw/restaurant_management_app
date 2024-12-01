package com.example.restaurantmanagementapp.apithings.schemasclasses

open class MealServer(
    open val id: Int,
    open val name: String,
    open val price: Double,
    open val photographUrl: String,
    open val ingredients: List<String>,
    open val removableIngredList: List<String>?,
    open val weightOrVolume: Double,
    open val unitType: String,
    open val categoryId: Int,
    open val allergens: List<String>,
    open val calories: Int,
    //open val unitTypeMandatory: Boolean,
)
