package com.example.restaurantmanagementapp.apithings.RequestClasses

data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val password: String,
    val admin: Boolean = false
)