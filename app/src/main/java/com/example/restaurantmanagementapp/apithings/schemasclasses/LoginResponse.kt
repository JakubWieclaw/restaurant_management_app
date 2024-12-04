package com.example.restaurantmanagementapp.apithings.schemasclasses

data class LoginResponse (
    val token: String,
    val customerId: Int,
    var customerName:String,
    var customerSurname:String,
    var customerEmail:String,
    val isAdmin: Boolean
)