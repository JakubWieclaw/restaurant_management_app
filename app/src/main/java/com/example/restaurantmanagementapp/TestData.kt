package com.example.restaurantmanagementapp

import com.example.restaurantmanagementapp.classes.Meal
import com.example.restaurantmanagementapp.classes.Opinion

object TestData {
    val mealListSample = listOf(
        Meal(
            0,
            "Pierogi",
            6.99,
            0,
            0,
            0,
            "NoneNow"
        ),
        Meal(
            1,
            "Spaghetti",
            4.99,
            1,
            1,
            1,
            "NoneNow"
        ),
        Meal(
            2,
            "Ciastka",
            16.99,
            0,
            2,
            2,
            "NoneNow"
        ),
        Meal(
            3,
            "Murzynek",
            13.99,
            0,
            3,
            3,
            "NoneNow"
        ),
        Meal(
            4,
            "Kiełbasa śląska",
            16.99,
            2,
            4,
            4,
            "NoneNow"
        ),
        Meal(
            5,
            "Pomidor",
            6.99,
            3,
            5,
            5,
            "NoneNow"
        )
    )

    val opinionsListSample = listOf(
        Opinion("Geralt z Rivii",  "Bardzo dobre danie, zawsze gdy jestem w Wyzimie odwiedzam to miejsce i próbuję tej potrawy.",5),
        Opinion("USERNAME",  "opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion",4),
        Opinion("USERNAME",  "opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion",3),
        Opinion("USERNAME",  "opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion",2)
    )
}