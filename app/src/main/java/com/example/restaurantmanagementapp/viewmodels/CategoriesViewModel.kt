package com.example.restaurantmanagementapp.viewmodels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RequestClasses.Category
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CategoriesViewModel : ViewModel() {
    var categoriesState by mutableStateOf<List<Category>?>(null)
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchCategories(onComplete: () -> Unit) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getCategories()
                response.enqueue(
                    CallbackHandler(
                        onSuccess = { responseBody ->
                            println("Odpowiedź: $responseBody")
                            try {
                                val gson = Gson()
                                val categoryListType = object : TypeToken<List<Category>>() {}.type
                                val categories: List<Category> = gson.fromJson(responseBody, categoryListType)
                                categories.forEach { category ->
                                    println("Category: ${category.name}, URL: ${category.photographUrl}")
                                }
                                categoriesState = categories
                                isLoading = false
                                onComplete()
                            } catch (e: Exception) {
                                println("Parsing error: ${e.message}")
                                categoriesState = emptyList()
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
