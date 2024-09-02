package com.example.restaurantmanagementapp.LoginScreen

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
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
                navigateToScreen("meallist",navController)
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
                navigateToScreen("meallist",navController)
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
    var isLoginSelected by remember { mutableStateOf(true) }

    Surface(color = MaterialTheme.colorScheme.background){
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)){
            Image(
                painter = painterResource(id = R.drawable.tmpimg),
                contentDescription = stringResource(id = R.string.logo_image_description),
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){

                Button(
                    onClick = { isLoginSelected = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLoginSelected) Color.Blue else Color.Gray,
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Logowanie", color = Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp)) // Przestrzeń między przyciskami

                Button(
                    onClick = { isLoginSelected = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isLoginSelected) Color.Blue else Color.Gray,
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Rejestracja", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (isLoginSelected) {
                LoginFields(navController, authViewModel)
            } else {
                RegisterFields(navController, authViewModel)
            }
        }
    }
}

@Composable
fun LoginFields(navController: NavController,authViewModel: AuthViewModel){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.SpaceBetween){
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = { Text("email") },
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                label = { Text("password") },
                maxLines = 1
            )

        }
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally).width(160.dp).height(60.dp).padding(top=8.dp),
            onClick = { login(email, password, navController, authViewModel) }) {
            Text("log in")
        }
    }
}


@Composable
fun RegisterFields(navController: NavController,authViewModel: AuthViewModel){
    var name by remember { mutableStateOf("")}
    var surname by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("")}
    var phone by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    Column(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.SpaceBetween){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it},
                label = { Text("Name") },
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = surname,
                onValueChange = { surname = it},
                label = { Text("surname") },
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it},
                label = { Text("email") },
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phone,
                onValueChange = { phone = it},
                label = { Text("phone") },
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it},
                label = { Text("password") },
                maxLines = 1
            )
        }
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally).width(160.dp).height(60.dp).padding(top=8.dp),
            onClick= {register(name,surname,email,phone,password,navController,authViewModel)}){
            Text("register")
        }
    }
}



