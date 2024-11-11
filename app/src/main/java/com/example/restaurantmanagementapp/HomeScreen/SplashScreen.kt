package com.example.restaurantmanagementapp.HomeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.apithings.schemasclasses.AvgRating
import com.example.restaurantmanagementapp.viewmodels.CategoriesViewModel
import com.example.restaurantmanagementapp.viewmodels.CouponsViewModel
import com.example.restaurantmanagementapp.viewmodels.MealsViewModel
import com.example.restaurantmanagementapp.apithings.schemasclasses.Opinion
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.min

@Composable
fun SplashScreen(categoriesViewModel: CategoriesViewModel, mealsViewModel: MealsViewModel, couponsViewModel: CouponsViewModel, navController: NavController) {
    var totalItems by remember { mutableIntStateOf(4) }
    var completed by remember { mutableIntStateOf(0) }

    var mealSize by remember {mutableIntStateOf(0)}
    var completed2 by remember{ mutableIntStateOf(0)}

    var mealsReady by remember { mutableStateOf(false) }

    var viewModelsReady by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(!viewModelsReady) {
        if(!viewModelsReady) {
            categoriesViewModel.fetchCategories(onComplete = { completed++ })
            mealsViewModel.fetchMeals(onComplete = { completed++;mealsReady = true })

            //TODO: Poprawić customer id
            couponsViewModel.fetchCoupons(customerId = 1,onComplete = {completed++})
        }
        viewModelsReady = true
    }

    LaunchedEffect(mealsReady) {
        if (mealsReady) {
            //totalItems += mealsViewModel.meals.size
            mealsViewModel.meals.forEach { meal ->
                mealsViewModel.downloadAndSaveImage(context = context,meal.photographUrl,onComplete={})
                val avgRatingResponse = RetrofitInstance.api.getAvgRating(meal.id)
                avgRatingResponse.enqueue(
                    CallbackHandler(
                        onSuccess = { responseBody ->
                            try {
                                val objectType = object : TypeToken<AvgRating>() {}.type
                                val avgRating: AvgRating = Gson().fromJson(responseBody, objectType)
                                meal.avgRating = avgRating.averageRating
                                completed2++
                            } catch (e: Exception) {
                                println("Parsing error: ${e.message}")
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
                //totalItems += mealsViewModel.meals.size
                val opinionsResponse = RetrofitInstance.api.getOpinionsForMeal(meal.id)
                opinionsResponse.enqueue(
                    CallbackHandler(
                        onSuccess = { responseBody ->
                            try {
                                val objectType = object : TypeToken<List<Opinion>>() {}.type
                                val opinionList: List<Opinion> =
                                    Gson().fromJson(responseBody, objectType)
                                opinionList.forEach { opinion ->
                                    println("Opinion: ${opinion.customerId}, ${opinion.rating}")
                                }
                                meal.opinions = opinionList
                                completed2++
                            } catch (e: Exception) {
                                println("Parsing error: ${e.message}")
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
            }
            mealsReady = false
            mealSize = 2*mealsViewModel.meals.size
        }
    }

    if(mealSize in 1..completed2){
        completed++
    }


    val progress = completed.toFloat() / totalItems.toFloat()

    if (completed >= totalItems) {
        navigateToScreen("restaurantinfo", navController)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.test_meal_picture_1),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id =  R.string.downloaded) + " ${min((progress * 100).toInt(),100)}%", style = Typography.displayLarge)
        }
    }
}
