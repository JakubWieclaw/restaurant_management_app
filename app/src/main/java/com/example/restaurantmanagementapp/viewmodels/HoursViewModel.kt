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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HoursViewModel: ViewModel() {
    var localTimes by mutableStateOf<List<String>?>(null)
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchAvailableHours(day:String,numberOfPeople:String,token:String){
        try {
            val response = RetrofitInstance.api.getAvailableHoursForDay(day,120,15,numberOfPeople.toInt(),"Bearer $token")
            response.enqueue(
                CallbackHandler(
                    onSuccess = { responseBody ->
                        println("Odpowiedź: $responseBody")
                        try {
                            val gson = Gson()
                            val localTimeListType = object : TypeToken<List<String>>() {}.type
                            localTimes  = gson.fromJson(responseBody, localTimeListType)
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
}