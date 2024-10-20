package com.example.restaurantmanagementapp.apithings.schemasclasses

class OrderAddCommand(
    val mealIds: List<MealQuantity>,
    val customerId: Int,
    val type: String,
    val status: String,
    val unwantedIngredient: UnwantedIngredient,
    val deliveryAddress: String,
    val deliveryDistance: Double
)