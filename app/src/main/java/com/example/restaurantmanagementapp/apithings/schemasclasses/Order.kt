package com.example.restaurantmanagementapp.apithings.schemasclasses

data class Order(
    val id: Int,
    val mealIds: List<MealQuantity>,
    val orderPrice: Double,
    val customerId: Int,
    val type: String,
    val status: String,
    val dateTime: String,
    val unwantedIngredients: List<UnwantedIngredient>,
    val deliveryAddress: String,
    val deliveryDistance: Double,
    val deliveryPrice: Double,
    val tableReservation: TableReservation
)
