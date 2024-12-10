package com.example.restaurantmanagementapp.HomeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.restaurantmanagementapp.TestData
import com.example.restaurantmanagementapp.TestData.mealListSample
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.apithings.schemasclasses.AvgRating
import com.example.restaurantmanagementapp.viewmodels.CategoriesViewModel
import com.example.restaurantmanagementapp.viewmodels.CouponsViewModel
import com.example.restaurantmanagementapp.viewmodels.MealsViewModel
import com.example.restaurantmanagementapp.apithings.schemasclasses.Opinion
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel
import com.example.restaurantmanagementapp.viewmodels.FavMealsViewModel
import com.example.restaurantmanagementapp.viewmodels.HoursViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.abs
import kotlin.math.min

@Composable
fun SplashScreen(categoriesViewModel: CategoriesViewModel, mealsViewModel: MealsViewModel, couponsViewModel: CouponsViewModel, authViewModel: AuthViewModel, favMealsViewModel: FavMealsViewModel,
                 hoursViewModel: HoursViewModel,images: List<String>, navController: NavController) {
    val totalItems by remember { mutableIntStateOf(3) }
    var completed by remember { mutableIntStateOf(0) }

    var mealSize by remember {mutableIntStateOf(100)}
    var completed2 by remember{ mutableIntStateOf(0)}

    var mealsReady by remember { mutableStateOf(false) }
    var viewModelsReady by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Pobranie obrazka lokalizacji
    mealsViewModel.downloadAndSaveImage(context,"lokalizacja.jpg", onComplete = {})

    // Pobranie galerii
    for (image in images) {
        mealsViewModel.downloadAndSaveImage(context,image, onComplete = {})
    }




    LaunchedEffect(!viewModelsReady) {
        authViewModel.setContext(context)
        //TODO: W zasadzie bezparametrowe onComplete wystarczy, bo wtedy authviewmodel.customerdata na pewno istnieje i można z niego skorzystać
        authViewModel.loginFromSavedData(onComplete = {token,id ->
            couponsViewModel.fetchCoupons(
            customerId = id,
            onComplete = {},
            token = token);
            hoursViewModel.getReservations(
                customerId = id,
                customerToken = token)
        })

        if(!viewModelsReady) {
            categoriesViewModel.fetchCategories(onComplete = { completed++ })
            mealsViewModel.fetchMeals(onComplete = { completed++;mealsReady = true })
        }
        viewModelsReady = true
    }

    LaunchedEffect(mealsReady) {
        if (mealsReady) {
            println("Pobieram obrazy, opinie i średnie oceny posiłków:")
            mealsViewModel.meals.forEach { meal ->
                mealsViewModel.downloadAndSaveImage(context = context,meal.photographUrl,onComplete={completed2++})

                val avgRatingResponse = RetrofitInstance.api.getAvgRating(meal.id)
                avgRatingResponse.enqueue(
                    CallbackHandler(
                        onSuccess = { responseBody ->
                            try {
                                val objectType = object : TypeToken<AvgRating>() {}.type
                                val avgRating: AvgRating = Gson().fromJson(responseBody, objectType)
                                meal.avgRating = avgRating.averageRating
                                println("\t* Pobrano oceny dla ${meal.name}")
                                completed2++
                            } catch (e: Exception) {
                                println("\t* Parsing error: ${e.message}")
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
                val opinionsResponse = RetrofitInstance.api.getOpinionsForMeal(meal.id)
                opinionsResponse.enqueue(
                    CallbackHandler(
                        onSuccess = { responseBody ->
                            try {
                                val objectType = object : TypeToken<List<Opinion>>() {}.type
                                val opinionList: List<Opinion> =
                                    Gson().fromJson(responseBody, objectType)
                                opinionList.forEach { opinion ->
                                    println("\t* Pobrano opinie dla ${meal.name} od ${opinion.customerId}, ${opinion.rating}")
                                }
                                meal.opinions = opinionList
                                completed2++
                            } catch (e: Exception) {
                                println("\t* Parsing error: ${e.message}")
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
            }
            mealsReady = false
            mealSize = 3*mealsViewModel.meals.size

            favMealsViewModel.setContext(context)
            favMealsViewModel.loadFavMeals(mealsViewModel.meals)
        }
    }

    val progress = (completed.toFloat() + completed2.toFloat()/mealSize.toFloat()) / totalItems.toFloat()

    //if(true){
    if (abs(progress - 1) < 0.001) {
        //categoriesViewModel.categoriesState = TestData.categories
        //mealsViewModel.meals = TestData.mealListSample
        //mealsViewModel.meals = mealListSample
        navigateToScreen("restaurantinfo", navController)
        //navigateToScreen("passwordreset",navController)
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoicon),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.size(60.dp),
                strokeWidth = 8.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.downloaded) + " ${min((progress * 100).toInt(), 100)}%",
                style = Typography.displayLarge
            )
        }
    }
}
