package com.example.restaurantmanagementapp.LoginScreen

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.LoginRequest
import com.example.restaurantmanagementapp.apithings.RegisterRequest
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.classes.AuthViewModel
import com.example.restaurantmanagementapp.ui.theme.RestaurantManagementAppTheme

//@Preview
//@Composable
//fun LoginScreenPreview(){
//    RestaurantManagementAppTheme {
//        LoginScreen()
//    }
//}

fun register(name:String,surname:String,email:String,phone:String,password:String,navController: NavController, authViewModel:AuthViewModel){
    val registerRequest = RegisterRequest(0,name,surname,email,phone,password)
    val call = RetrofitInstance.api.register(registerRequest)

    call.enqueue(
        CallbackHandler(
            onSuccess = { responseBody ->
                println("Odpowiedź: $responseBody")
                authViewModel.login(responseBody)
                navController.navigate("meallist")
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
fun login(email:String,password:String,navController: NavController,authViewModel: AuthViewModel){
    val loginRequest = LoginRequest(email,password)
    val call = RetrofitInstance.api.login(loginRequest)

    call.enqueue(
        CallbackHandler(
            onSuccess = { responseBody ->
                println("Odpowiedź: $responseBody")
                authViewModel.login(responseBody)
                navController.navigate("meallist")
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
fun LoginScreen(modifier: Modifier = Modifier,navController: NavController, authViewModel: AuthViewModel){
    var name by remember { mutableStateOf("")}
    var surname by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("")}
    var phone by remember { mutableStateOf("")}
    var password1 by remember { mutableStateOf("")}

    var login by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }

    Surface(color = MaterialTheme.colorScheme.background){
        Column{
            Image(
                painter = painterResource(id = R.drawable.tmpimg),
                contentDescription = stringResource(id = R.string.logo_image_description)
            )
            Row(modifier = Modifier.fillMaxWidth()){
                Column(modifier = Modifier.weight(0.5f)){
                    TextField(
                        value = name,
                        onValueChange = { name = it},
                        label = { Text("Name") }
                    )
                    TextField(
                        value = surname,
                        onValueChange = { surname = it},
                        label = { Text("surname") }
                    )
                    TextField(
                        value = email,
                        onValueChange = { email = it},
                        label = { Text("email") }
                    )
                    TextField(
                        value = phone,
                        onValueChange = { phone = it},
                        label = { Text("phone") }
                    )
                    TextField(
                        value = password1,
                        onValueChange = { password1 = it},
                        label = { Text("password") }
                    )
                    Button(onClick= {register(name,surname,email,phone,password1,navController,authViewModel)}){
                        Text("register")
                    }
                }
                Spacer(modifier = Modifier.fillMaxHeight().padding(2.dp))
                Column(modifier = Modifier.weight(0.5f)){
                    TextField(
                        value = email,
                        onValueChange = { email = it},
                        label = { Text("email") }
                    )
                    TextField(
                        value = password2,
                        onValueChange = { password2 = it},
                        label = { Text("password") }
                    )
                    Button(onClick= {login(email,password2,navController,authViewModel)}){
                        Text("login")
                    }
                }
            }

        }
    }
}


