package com.example.restaurantmanagementapp

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
import com.example.restaurantmanagementapp.ui.theme.RestaurantManagementAppTheme

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
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RestaurantManagementAppTheme {
        Greeting("Android")
    }
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
            Button(onClick= {}){
                Text("Log in")
            }
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview(){
    RestaurantManagementAppTheme {
        LoginScreen()
    }
}
