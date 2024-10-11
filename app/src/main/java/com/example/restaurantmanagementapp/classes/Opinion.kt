package com.example.restaurantmanagementapp.classes

class Opinion(
    val customerId: Int,
    val rating: Int,
    val comment: String
)

data class AvgRating(
    val averageRating: Double,
    val numberOfOpinions: Int
)