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
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.restaurantmanagementapp.apithings.CallbackHandlerImage
import kotlinx.coroutines.CoroutineStart
import java.io.File
import java.io.FileOutputStream
import kotlin.io.encoding.ExperimentalEncodingApi


class MealsViewModel : ViewModel() {
    var meals by mutableStateOf<List<Meal>>(emptyList())
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchMeals(onComplete: () -> Unit) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch(Dispatchers.IO) {
            println("Pobieram posiłki:")
            try {
                val response = RetrofitInstance.api.getMeals()
                response.enqueue(
                    CallbackHandler(
                        onSuccess = { responseBody ->
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
                                        //unitTypeMandatory = mealServer.unitTypeMandatory,
                                        quantity2 = 1
                                    )
                                }
                                meals2.forEach { meal ->
                                    println("\t* Meal: ${meal.name}, category: ${meal.categoryId}, photo URL: ${meal.photographUrl}")
                                }
                                meals = meals3
                                isLoading = false
                                onComplete()
                            } catch (e: Exception) {
                                println("\t* Parsing error: ${e.message}")
                                errorMessage = "\t* Error: ${e.message}"
                            }
                        },
                        onError = { code, errorBody ->
                            println("\t* Błąd: $code")
                            println("\t* Treść błędu: $errorBody")
                        },
                        onFailure = { throwable ->
                            println("\t* Request failed: ${throwable.message}")
                        }
                    )
                )
            } catch (e: Exception) {
                errorMessage = "\t* Error: ${e.message}"
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun downloadAndSaveImage(context:Context, filename:String, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            var file = File(context.filesDir, filename)
            if (!file.exists()) {
                try {
                    val response = RetrofitInstance.api.getPhoto(filename = filename)
                    response.enqueue(
                        CallbackHandlerImage(
                            onSuccess = { responseBody ->
                                try {
                                    file = File(context.filesDir, filename)
                                    FileOutputStream(file).use{ outputStream ->
                                        outputStream.write(responseBody)
                                        outputStream.flush()
                                    }
                                    println("\t* Pobrano obraz $filename")
                                    onComplete()
                                } catch (e: Exception) {
                                    println("\t* Parsing error: ${e.message}")
                                    errorMessage = "\t* Error: ${e.message}"
                                }
                            },
                            onError = { code, errorBody ->
                                println("\t* Błąd: $code")
                                println("\t* Treść błędu: $errorBody")
                            },
                            onFailure = { throwable ->
                                println("\t* Request failed: ${throwable.message}")
                            }
                        )
                    )
                } catch (e: Exception) {
                    errorMessage = "\t* Error: ${e.message}"
                }
            }else{
                println("\t* Obraz $filename znajduje się już na urządzeniu")
            }
            onComplete()
        }
    }


    fun findMeal(mealId:Int):Meal?{
        return meals.find{it.id == mealId}
    }
}

fun loadImageFromDevice(context: Context, filename: String): ImageBitmap? {
    val file = File(context.filesDir, filename)
    return if (file.exists()) {
        println("Wczytywanie $filename")
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        bitmap?.asImageBitmap()
    } else {
        println("Plik $filename nie istnieje")
        null
    }
}