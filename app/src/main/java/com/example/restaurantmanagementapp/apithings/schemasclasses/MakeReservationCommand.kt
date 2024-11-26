package com.example.restaurantmanagementapp.apithings.schemasclasses

data class MakeReservationCommand (
    val day:String,
    val startTime:String,
    val endTime:String,
    val numberOfPeople:Int,
    val customerId:Int
)