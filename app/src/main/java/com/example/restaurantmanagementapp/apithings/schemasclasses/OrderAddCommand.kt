package com.example.restaurantmanagementapp.apithings.schemasclasses

class OrderAddCommand(
    val mealIds: List<MealQuantity>,
    val customerId: Int,
    val type: String,
    val status: String,
    val unwantedIngredients: List<UnwantedIngredient>,
    val deliveryAddress: String,
    val deliveryDistance: Double,
    val tableId: String,
    val people: Int,
    val minutesForReservation: Int,
    val couponCode: String?
)