package com.example.restaurantmanagementapp.LoginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RegisterRequest
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.ui.theme.RestaurantManagementAppTheme

@Preview
@Composable
fun LoginScreenPreview(){
    RestaurantManagementAppTheme {
        LoginScreen()
    }
}

fun register(username:String, password:String){
    val registerRequest = RegisterRequest(0,username,"string","string","string",password)
    val call = RetrofitInstance.api.register(registerRequest)

    call.enqueue(
        CallbackHandler(
            onSuccess = { responseBody ->
                println("Odpowiedź: $responseBody")
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


