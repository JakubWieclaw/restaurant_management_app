package com.example.restaurantmanagementapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.apithings.schemasclasses.MealServer
import com.example.restaurantmanagementapp.classes.Meal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MealsViewModel : ViewModel() {
    var meals by mutableStateOf<List<Meal>>(emptyList())
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchMeals(onComplete: () -> Unit) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getMeals()
                response.enqueue(
                    CallbackHandler(
                        onSuccess = { responseBody ->
                            println("Odpowiedź: $responseBody")
                            try {
                                val mealListType = object : TypeToken<List<MealServer>>() {}.type
                                val meals2: List<MealServer> = Gson().fromJson(responseBody, mealListType)
                                val meals3: List<Meal> = meals2.map{ mealServer ->
                                    Meal(
                                        id = mealServer.id,
                                        name = mealServer.name,
                                        price = mealServer.price,
                                        photographUrl = mealServer.photographUrl,
                                        ingredients = mealServer.ingredients,
                                        weightOrVolume = mealServer.weightOrVolume,
                                        unitType = mealServer.unitType,
                                        categoryId = mealServer.categoryId,
                                        allergens = mealServer.allergens,
                                        calories = mealServer.calories,
                                        unitTypeMandatory = mealServer.unitTypeMandatory,
                                        quantity2 = 1
                                    )
                                }
                                meals2.forEach { meal ->
                                    println("Category: ${meal.name}, URL: ${meal.photographUrl}")
                                }
                                meals = meals3
                                isLoading = false
                                onComplete()
                            } catch (e: Exception) {
                                println("Parsing error: ${e.message}")
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
}
