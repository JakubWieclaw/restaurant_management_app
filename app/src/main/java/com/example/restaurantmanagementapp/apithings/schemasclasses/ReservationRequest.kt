package com.example.restaurantmanagementapp.apithings.schemasclasses


class ReservationRequest(
    val day: String,
    val startTime: LocalTime,
    val endTime:LocalTime,
    val numberOfPeople:Int,
    val customerId: Int
)