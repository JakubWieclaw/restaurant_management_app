package com.example.restaurantmanagementapp.LoginScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RequestClasses.LoginRequest
import com.example.restaurantmanagementapp.apithings.RequestClasses.RegisterRequest
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel

//@Preview
//@Composable
//fun LoginScreenPreview(){
//    RestaurantManagementAppTheme {
//        LoginScreen()
//    }
//}

fun register(name:String,surname:String,email:String,phone:String,password:String,navController: NavController, authViewModel: AuthViewModel){
    val registerRequest = RegisterRequest(name,surname,email,phone,password)
    authViewModel.register(registerRequest)
    navigateToScreen("meallist",navController)
}

fun login(email:String,password:String,navController: NavController,authViewModel: AuthViewModel){
    val loginRequest = LoginRequest(email,password)
    authViewModel.login(loginRequest)
    navigateToScreen("meallist",navController)
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier,navController: NavController, authViewModel: AuthViewModel){
    var isLoginSelected by remember { mutableStateOf(true) }
    var isForgotSelected by remember { mutableStateOf(false) }
    var emailForgot by remember { mutableStateOf("") }
    val context = LocalContext.current
    val elementsStyle = Typography.bodyMedium

    Surface(color = MaterialTheme.colorScheme.background){
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)){
            Image(
                painter = painterResource(id = R.drawable.tmpimg),
                contentDescription = stringResource(id = R.string.logo_image_description),
                modifier = Modifier
                    .size(140.dp)
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
                    Text(text = stringResource(id = R.string.loging), color = Color.White, style = Typography.titleMedium)
                }

                Spacer(modifier = Modifier.width(16.dp)) // Przestrzeń między przyciskami

                Button(
                    onClick = { isLoginSelected = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isLoginSelected) Color.Blue else Color.Gray,
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text =stringResource(id = R.string.registration), color = Color.White, style = Typography.titleMedium)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (isLoginSelected) {
                LoginFields(navController, authViewModel, elementsStyle)
            } else {
                RegisterFields(navController, authViewModel, elementsStyle)
            }
            Spacer(modifier = Modifier.height(16.dp))

            val annotatedString = buildAnnotatedString {
                append(stringResource(id = R.string.forgotpassword))
                addStyle(
                    style = SpanStyle(
                        color = Color.Blue, // Kolor linku
                        textDecoration = TextDecoration.Underline, // Podkreślenie
                        fontSize = 20.sp
                    ),
                    start = 0,
                    end = this.length
                )
            }
            ClickableText(text=annotatedString, onClick={isForgotSelected=!isForgotSelected}, modifier = Modifier.align(Alignment.CenterHorizontally))
            if(isForgotSelected){
                Row(horizontalArrangement = Arrangement.Center){
                    OutlinedTextField(
                        modifier = Modifier.weight(0.5f),
                        value = emailForgot,
                        onValueChange = {emailForgot = it},
                        label = {Text(text = stringResource(id = R.string.email), style = elementsStyle)},
                        maxLines = 2
                    )
                    Button(onClick = {
                        val call = RetrofitInstance.api.forgotPassword(email = emailForgot)
                        call.enqueue(
                            CallbackHandler(
                                onSuccess = { _ ->
                                    isForgotSelected=false
                                    Toast.makeText(context,"Wysłano maila",Toast.LENGTH_LONG).show()
                                },
                                onError = { _, _->
                                    Toast.makeText(context,"Błąd, nie udało się wysłać maila",Toast.LENGTH_LONG).show()
                                },
                                onFailure = { _ ->
                                    Toast.makeText(context,"Błąd, nie udało się wysłać maila",Toast.LENGTH_LONG).show()
                                }
                            )
                        )

                    }){
                        Text(text = stringResource(id = R.string.reset), style = elementsStyle)
                    }
                }
            }
        }
    }
}

@Composable
fun LoginFields(navController: NavController,authViewModel: AuthViewModel, elementsStyle: TextStyle){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxWidth(),verticalArrangement = Arrangement.SpaceBetween){
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(id = R.string.email), style = elementsStyle) },
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(id = R.string.password), style = elementsStyle) },
                maxLines = 1
            )

        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(160.dp)
                .height(60.dp)
                .padding(top = 8.dp),
            onClick = { login(email, password, navController, authViewModel) }) {
            Text(text = stringResource(id = R.string.log_in), style = Typography.titleMedium)
        }
    }
}


@Composable
fun RegisterFields(navController: NavController,authViewModel: AuthViewModel, elementsStyle: TextStyle){
    var name by remember { mutableStateOf("")}
    var surname by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("")}
    var phone by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    Column(modifier = Modifier.fillMaxWidth(),verticalArrangement = Arrangement.SpaceBetween){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it},
                label = { Text(text = stringResource(id = R.string.name), style = elementsStyle) },
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = surname,
                onValueChange = { surname = it},
                label = { Text(text = stringResource(id = R.string.surname), style = elementsStyle) },
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it},
                label = { Text(text = stringResource(id = R.string.email), style = elementsStyle) },
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phone,
                onValueChange = { phone = it},
                label = { Text(text = stringResource(id = R.string.phone), style = elementsStyle) },
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it},
                label = { Text(text = stringResource(id = R.string.password), style = elementsStyle) },
                maxLines = 1
            )
        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(160.dp)
                .height(60.dp)
                .padding(top = 8.dp),
            onClick= {register(name,surname,email,phone,password,navController,authViewModel)}){
            Text(text = stringResource(id = R.string.register), style = Typography.titleMedium)
        }
    }
}



