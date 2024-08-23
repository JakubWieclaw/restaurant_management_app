package com.example.restaurantmanagementapp.LoginScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurantmanagementapp.MealScreen.MealList
import com.example.restaurantmanagementapp.MealScreen.MealListPreview
import com.example.restaurantmanagementapp.MealScreen.MealScreen
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.RestaurantInfoScreen.RestaurantInfo
import com.example.restaurantmanagementapp.TestData
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.LoginRequest
import com.example.restaurantmanagementapp.apithings.RegisterRequest
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.ui.theme.RestaurantManagementAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import com.google.gson.*
import okhttp3.ResponseBody

@Preview
@Composable
fun LoginScreenPreview(){
    RestaurantManagementAppTheme {
        LoginScreen()
    }
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurantManagementAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TestMainScreen()
                }
            }
        }
    }
}

@Composable
fun TestMainScreen(){
    val navController = rememberNavController()
    val meals = TestData.mealListSample
    val categories = TestData.categories
    val images = TestData.imagesList
    NavHost(
        navController = navController, startDestination = "restaurantinfo"
    ){
        composable("restaurantinfo"){ RestaurantInfo(images = images, navController = navController)}
        composable("meallist"){ MealList(meals,categories,navController)}
        composable("meal/{mealId}"){ backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId")
            val meal = meals.find{it.id.toString() == mealId}!!
            MealScreen(meal,navController)
        }
        composable("loginscreen"){LoginScreen()}
    }
}

fun register(username:String, password:String){
    val registerRequest = RegisterRequest(0,username,"string","string","string",password)
    val call = RetrofitInstance.api.register(registerRequest)

    call.enqueue(
        CallbackHandler(
            onSuccess = { responseBody ->
                println("Odpowiedź: $responseBody")
                // Tutaj możesz dodać dodatkowe akcje, np. zapis do bazy danych lub nawigację do innego ekranu
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

@Composable
fun LoginScreen(modifier: Modifier = Modifier){
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(color = MaterialTheme.colorScheme.background){
        Column{
            Image(
                painter = painterResource(id = R.drawable.tmpimg),
                contentDescription = stringResource(id = R.string.logo_image_description)
            )
            TextField(
                value = login,
                onValueChange = { login = it},
                label = { Text("Login") }
            )
            TextField(
                value = password,
                onValueChange = { password = it},
                label = { Text("Password") }
            )
            Button(onClick= {register(login,password)}){
                Text("Log in")
            }
        }
    }
}


