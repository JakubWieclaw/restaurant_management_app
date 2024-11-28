package com.example.restaurantmanagementapp.UserPanelScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.apithings.schemasclasses.ContactFormCommand
import com.example.restaurantmanagementapp.apithings.schemasclasses.Order
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//TODO: Dokończyć
@Composable
fun SuggestionForm(authViewModel: AuthViewModel, navController: NavController){
    var message by remember{mutableStateOf("")}
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        TextField(
            value=message,
            onValueChange = {newValue -> message = newValue },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp).padding(vertical = 16.dp).height(500.dp),
            label = {Text("Twoja wiadomość")},
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White)
        )
        Button(onClick={
            val contactForm = ContactFormCommand(name = authViewModel.customerData!!.customerName,email = authViewModel.customerData!!.customerEmail,message = message)
            try{
                val response = RetrofitInstance.api.createContactForm(contactForm)
                response.enqueue(
                    CallbackHandler(
                        onSuccess = { responseBody ->
                            println("Odpowiedz: $responseBody")
                            navigateToScreen("userpanel",navController)
                            Toast.makeText(context,"Dziękujęmy za wiadomość",Toast.LENGTH_LONG).show()
                        },
                        onError = {code, errorBody ->
                            println("Błąd: $code")
                            println("Treść błędu: $errorBody")},
                        onFailure = {throwable ->
                            println("Request failed: ${throwable.message}")}
                    )
                )

            }catch (e: Exception){
                println("Error: ${e.message}")
            }
        }){
            Text("Wyślij wiadomość")
        }
    }
}