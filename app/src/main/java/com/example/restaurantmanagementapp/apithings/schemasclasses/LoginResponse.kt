package com.example.restaurantmanagementapp.apithings.schemasclasses

class LoginResponse (
    val token: String,
    val customerId: Int,
    val customerName:String,
    val customerSurname:String,
    val customerEmail:String,
    val isAdmin: Boolean
)