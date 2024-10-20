package com.example.restaurantmanagementapp.apithings.RequestClasses

import com.example.restaurantmanagementapp.apithings.schemasclasses.CustomerServer
import com.example.restaurantmanagementapp.apithings.schemasclasses.MealServer

data class CouponServer(
    val id: Int,
    val code:String,
    val discountPercentage: Double,
    val customer: CustomerServer,
    val meal: MealServer,
    val expiryDate: String,
    val active: Boolean
)