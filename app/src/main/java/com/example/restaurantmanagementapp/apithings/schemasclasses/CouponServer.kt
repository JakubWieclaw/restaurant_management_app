package com.example.restaurantmanagementapp.apithings.schemasclasses

data class CouponServer(
    val id: Int,
    val code:String,
    val discountPercentage: Double,
    val customer: CustomerServer,
    val meal: MealServer,
    val expiryDate: String,
    val active: Boolean
)