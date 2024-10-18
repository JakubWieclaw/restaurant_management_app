package com.example.restaurantmanagementapp.classes

data class CouponServer(
    val id: Int,
    val code:String,
    val discountPercentage: Double,
    val customer: CustomerServer,
    val meal: MealServer,
    val expiryDate: String,
    val active: Boolean
)