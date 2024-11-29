package com.example.restaurantmanagementapp.LoginScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
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
import com.example.restaurantmanagementapp.HomeScreen.CustomBackground
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

fun login(email:String,password:String,navController: NavController,authViewModel: AuthViewModel,checked:Boolean){
    val loginRequest = LoginRequest(email,password)
    authViewModel.login(loginRequest,checked)
    navigateToScreen("meallist",navController)
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier,navController: NavController, authViewModel: AuthViewModel){
    var isLoginSelected by remember { mutableStateOf(true) }
    var isForgotSelected by remember { mutableStateOf(false) }
    var emailForgot by remember { mutableStateOf("") }
    val context = LocalContext.current
    val elementsStyle = Typography.bodyMedium
    var checked by remember { mutableStateOf(true) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)){
//        Image(
//            painter = painterResource(id = R.drawable.tmpimg),
//            contentDescription = stringResource(id = R.string.logo_image_description),
//            modifier = Modifier
//                .size(140.dp)
//                .clip(RoundedCornerShape(20.dp))
//                .fillMaxWidth()
//                .align(Alignment.CenterHorizontally)
//        )
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
                Text(text = stringResource(id = R.string.loging), color = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp)) // Przestrzeń między przyciskami

            Button(
                onClick = { isLoginSelected = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isLoginSelected) Color.Blue else Color.Gray,
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text =stringResource(id = R.string.registration), color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (isLoginSelected) {
            LoginFields(navController, authViewModel, elementsStyle, checked)
        } else {
            RegisterFields(navController, authViewModel, elementsStyle)
        }
        if(isLoginSelected){
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp)){
                Checkbox(checked = checked,onCheckedChange = { checked = it })
                Text(text = stringResource(id = R.string.dontlogout),style = elementsStyle,modifier = Modifier.align(Alignment.CenterVertically))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "Zapomniałem hasła",
            modifier = Modifier.clickable { navigateToScreen("passwordreset",navController) },
            textDecoration = TextDecoration.Underline,
            color = Color.Blue
        )
    }
}

@Composable
fun LoginFields(navController: NavController,authViewModel: AuthViewModel, elementsStyle: TextStyle, checked:Boolean){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxWidth(),verticalArrangement = Arrangement.SpaceBetween){
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(id = R.string.email), style = elementsStyle) },
                maxLines = 1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(id = R.string.password), style = elementsStyle) },
                maxLines = 1
            )

        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .height(60.dp)
                .padding(10.dp),
            onClick = { login(email, password, navController, authViewModel,checked) }) {
            Text(text = stringResource(id = R.string.log_in), style = Typography.labelLarge)
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
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it},
                label = { Text(text = stringResource(id = R.string.name), style = elementsStyle) },
                maxLines = 1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = surname,
                onValueChange = { surname = it},
                label = { Text(text = stringResource(id = R.string.surname), style = elementsStyle) },
                maxLines = 1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it},
                label = { Text(text = stringResource(id = R.string.email), style = elementsStyle) },
                maxLines = 1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = phone,
                onValueChange = { phone = it},
                label = { Text(text = stringResource(id = R.string.phone), style = elementsStyle) },
                maxLines = 1
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it},
                label = { Text(text = stringResource(id = R.string.password), style = elementsStyle) },
                maxLines = 1
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .height(60.dp)
                .padding(10.dp),
            onClick= {register(name,surname,email,phone,password,navController,authViewModel)}){
            Text(text = stringResource(id = R.string.register), style = Typography.labelLarge)
        }
    }
}



