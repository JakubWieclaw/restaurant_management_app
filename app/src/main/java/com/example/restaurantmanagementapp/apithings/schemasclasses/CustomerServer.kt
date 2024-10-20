package com.example.restaurantmanagementapp.apithings.schemasclasses

data class CustomerServer (
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val privilege: Privilege,
    val resetToken: String,
    val resetTokenExpiry: String
)