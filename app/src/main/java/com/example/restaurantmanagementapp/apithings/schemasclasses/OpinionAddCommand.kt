package com.example.restaurantmanagementapp.apithings.schemasclasses

class OpinionAddCommand(
    val mealId: Int,
    val customerId: Int,
    val rating: Int,
    val comment: String
)