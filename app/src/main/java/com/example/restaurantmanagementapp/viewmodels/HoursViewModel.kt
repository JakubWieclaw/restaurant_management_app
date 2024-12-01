package com.example.restaurantmanagementapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RequestClasses.Category
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.apithings.schemasclasses.LocalTime
import com.example.restaurantmanagementapp.apithings.schemasclasses.MakeReservationCommand
import com.example.restaurantmanagementapp.apithings.schemasclasses.TableReservation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HoursViewModel: ViewModel() {
    var localTimes by mutableStateOf<List<LocalTime>?>(null)
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)
    var tableReservations by mutableStateOf<List<TableReservation>>(emptyList())

    fun fetchAvailableHours(day:String,numberOfPeople:String,token:String) {
        try {
            println("Bearer $token")
            val response = RetrofitInstance.api.getAvailableHoursForDay(day,120,15,numberOfPeople.toInt(),"Bearer $token")
            response.enqueue(
                CallbackHandler(
                    onSuccess = { responseBody ->
                        println("Odpowiedź: $responseBody")
                        try {
                            val gson = Gson()
                            val localTimeListType = object : TypeToken<List<String>>() {}.type
                            val localTimesString:List<String> = gson.fromJson(responseBody, localTimeListType)
                            localTimes  = localTimesString.map{
                                val trimmed:List<String> = it.split(":")
                                LocalTime(hour = trimmed[0].toInt(), minute = trimmed[1].toInt(), second = trimmed[2].toInt(),nano=0)
                            }

                            isLoading = false
                        } catch (e: Exception) {
                            println("Parsing error: ${e.message}")
                            localTimes = emptyList()
                            errorMessage = "Error: ${e.message}"
                        }
                    },
                    onError = { code, errorBody ->
                        println("Błąd: $code")
                        println("Treść błędu: $errorBody")
                    },
                    onFailure = { throwable ->
                        println("Request failed: ${throwable.message}")
                    }
                )
            )
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
        }
    }

    fun makeReservation(day: String, startTime: LocalTime,numberOfPeople: Int, customerId: Int, customerToken: String) {
        val endTime = LocalTime((startTime.hour+2),startTime.minute,startTime.second,startTime.nano)
        val makeReservationCommand = MakeReservationCommand(
            day = day,
            startTime = "${startTime.hour}:${startTime.minute}:00",
            endTime = "${endTime.hour}:${endTime.minute}:00",
            numberOfPeople = numberOfPeople,
            customerId = customerId
        )
        try {
            println("Bearer $customerToken")
            println(makeReservationCommand)
            val response = RetrofitInstance.api.createReservation(makeReservationCommand,"Bearer $customerToken")
            response.enqueue(
                CallbackHandler(
                    onSuccess = { responseBody ->
                        println("Odpowiedź: $responseBody")
                        try {
                            val gson = Gson()
                            val tableReservationsType = object : TypeToken<TableReservation>() {}.type
                            tableReservations = listOf(gson.fromJson(responseBody, tableReservationsType))
                        } catch (e: Exception) {
                            println("Parsing error: ${e.message}")
                            tableReservations = emptyList()
                            errorMessage = "Error: ${e.message}"
                        }
                    },
                    onError = { code, errorBody ->
                        println("Błąd: $code")
                        println("Treść błędu: $errorBody")
                    },
                    onFailure = { throwable ->
                        println("Request failed: ${throwable.message}")
                    }
                )
            )
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
        }
    }

    fun cancelReservation(customerToken: String){
        if(tableReservations.isNotEmpty()){
            try {
                println("Bearer $customerToken")
                val response = RetrofitInstance.api.cancelReservation(tableReservations[0].id,"Bearer $customerToken")
                response.enqueue(
                    CallbackHandler(
                        onSuccess = { responseBody ->
                            println("Odpowiedź: $responseBody")
                            tableReservations = emptyList()
                        },
                        onError = { code, errorBody ->
                            println("Błąd: $code")
                            println("Treść błędu: $errorBody")
                            tableReservations = emptyList()
                        },
                        onFailure = { throwable ->
                            println("Request failed: ${throwable.message}")
                        }
                    )
                )
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            }
        }
    }

    fun getReservations(customerId: Int,customerToken: String): Boolean {
        try {
            println("Bearer $customerToken")
            val response = RetrofitInstance.api.getReservations(customerId,"Bearer $customerToken")
            response.enqueue(
                CallbackHandler(
                    onSuccess = { responseBody ->
                        println("Odpowiedź: $responseBody")
                        try {
                            val gson = Gson()
                            val objectType = object : TypeToken<List<TableReservation>>() {}.type
                            tableReservations = gson.fromJson(responseBody, objectType)
                        } catch (e: Exception) {
                            println("Parsing error: ${e.message}")
                            tableReservations = emptyList()
                            errorMessage = "Error: ${e.message}"
                        }
                    },
                    onError = { code, errorBody ->
                        println("Błąd: $code")
                        println("Treść błędu: $errorBody")
                        tableReservations = emptyList()
                    },
                    onFailure = { throwable ->
                        println("Request failed: ${throwable.message}")
                    }
                )
            )
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
        }
        return true
    }
}