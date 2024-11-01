package com.example.restaurantmanagementapp.apithings.schemasclasses

data class TableReservation(
    val id: Int,
    val tableId: Int,
    val people: Int,
    val day: String,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val duration: Int,
    val customerId: Int,
    val orders: Any
)