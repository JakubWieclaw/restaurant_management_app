package com.example.restaurantmanagementapp.apithings.schemasclasses

data class TableReservation(
    val id: Int,
    val tableId: String,
    val people: Int,
    val day: String,
    val startTime: String,
    val endTime: String,
    val duration: Int,
    val customerId: Int,
    val orders: List<Order>
)