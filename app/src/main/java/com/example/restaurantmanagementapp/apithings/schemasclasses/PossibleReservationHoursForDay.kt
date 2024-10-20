package com.example.restaurantmanagementapp.apithings.schemasclasses


class PossibleReservationHoursForDay(
    val date: String,
    val possibleStartTimes:List<LocalTime>
)