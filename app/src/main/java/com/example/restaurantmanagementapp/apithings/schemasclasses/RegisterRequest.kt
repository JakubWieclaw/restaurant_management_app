package com.example.restaurantmanagementapp.apithings.schemasclasses

data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    var password: String
)