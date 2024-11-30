package com.example.restaurantmanagementapp

import com.example.restaurantmanagementapp.apithings.RequestClasses.Category
import com.example.restaurantmanagementapp.apithings.schemasclasses.LoginResponse
import com.example.restaurantmanagementapp.apithings.schemasclasses.Opinion
import com.example.restaurantmanagementapp.classes.Meal
import com.example.restaurantmanagementapp.classes.Table

object TestData {
    val mealListSample = listOf(
        Meal(
            id=1,
            name="Spaghetti",
            price=4.99,
            photographUrl = "string",
            ingredients = listOf("ser,pieczarki"),
            removableIngredients = listOf("ser"),
            weightOrVolume = .3,
            unitType = "GRAMY",
            categoryId = 1,
            allergens = listOf("białko"),
            calories = 233
        )
    )

    val opinionsListSample = listOf(
        Opinion(2,5,  "Bardzo dobre danie, zawsze gdy jestem w Wyzimie odwiedzam to miejsce i próbuję tej potrawy."),
        Opinion(3,  4,"opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion"),
        Opinion(4,  3,"opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion"),
        Opinion(5,  2,"opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion opinion")
    )

    val categories = listOf(
        Category(1,"Cat1","string"),
        Category(2,"Cat2","string"),
        Category(3,"Cat3","string"),
        Category(4,"Cat4","string")
    )

    val availableTimes = listOf(
        "8:00",
        "8:20",
        "8:40",
        "9:00",
        "9:20",
        "9:40",
        "10:00",
        "10:20",
        "10:40",
        "11:00",
        "11:20",
        "11:40",
        "12:00",
        "12:20",
        "12:40",
        "13:00",
        "13:20",
        "13:40",
        "14:00",
        "14:20",
        "14:40",
        "15:00",
        "15:20",
        "15:40",
        "16:00",
        "16:20",
        "16:40",
        "17:00",
        "17:20",
        "17:40",
        "18:00",
        "18:20",
        "18:40",
        "19:00",
        "19:20",
        "19:40",
        "20:00",
        "20:20",
        "20:40",
        "21:00",
        "21:20",
        "21:40",
        "22:00",
        "22:20",
        "22:40",
    )

    val imagesList = listOf(
        R.drawable.test_meal_picture_1,
        R.drawable.test_meal_picture_1,
        R.drawable.test_meal_picture_1,
        R.drawable.test_meal_picture_1,
        R.drawable.test_meal_picture_1,
    )

//    val tablesList = listOf(
//        Table(1,2,"00:00","24:00"),
//        Table(2,4,"00:00","24:00"),
//        Table(3,6,"00:00","24:00"),
//        Table(4,2,"12:00","24:00"),
//        Table(5,4,"12:00","24:00"),
//        Table(6,6,"12:00","24:00"),
//        Table(7,2,"12:00","18:00"),
//        Table(8,4,"12:00","18:00"),
//        Table(9,6,"12:00","18:00"),
//    )

    val testUser =
        LoginResponse (
        token= "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhc2RAYXNkLmFzZCIsImlhdCI6MTczMjYyNzg3NiwiZXhwIjoxNzMyNjYzODc2fQ.QM31qKObZa4XXChgcziVOhVI8JDHMD6DYiqQLW6D-KlRkz68jtZsn30q80uITFeXOW7mrs3g-yLJ3XsC3IIlqQ",
        customerId= 1,
        customerName="asd",
        customerSurname="asd",
        customerEmail="asd@asd.asd",
        isAdmin= true
    )
    val restaurantDescription = "Odkryj kulinarną podróż w sercu miasta w restauracji Słoneczne Smaki! Serwujemy dania inspirowane kuchnią śródziemnomorską, z naciskiem na świeżość i najwyższą jakość składników. W naszym menu znajdziesz zarówno klasyczne włoskie makarony, jak i wykwintne owoce morza, soczyste steki oraz aromatyczne sałatki.\n" +
            "\n" +
            "Nasza przytulna atmosfera, klimatyczne wnętrze i przyjazna obsługa sprawią, że poczujesz się jak na wakacjach nad Morzem Śródziemnym. Każdego dnia oferujemy również specjalne menu lunchowe oraz unikalne propozycje sezonowe, które zaskoczą nawet najbardziej wymagających smakoszy.\n" +
            "\n" +
            "Zapraszamy na niezapomniany wieczór pełen smaków i aromatów!"

}