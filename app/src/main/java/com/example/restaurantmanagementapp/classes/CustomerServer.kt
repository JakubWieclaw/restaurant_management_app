package com.example.restaurantmanagementapp.classes

data class CustomerServer (
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val privilege: PrivilegeServer,
    val resetToken: String,
    val resetTokenExpiry: String
)