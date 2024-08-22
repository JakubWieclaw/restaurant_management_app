package com.example.restaurantmanagementapp.apithings

data class RegisterRequest(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val password: String,
)