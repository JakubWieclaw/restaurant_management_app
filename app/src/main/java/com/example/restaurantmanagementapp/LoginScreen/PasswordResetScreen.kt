package com.example.restaurantmanagementapp.LoginScreen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RetrofitInstance

@Composable
fun PasswordResetScreen(navController: NavController) {
    var currentStep by remember { mutableStateOf(1) } // Śledzi aktualny krok
    var email by remember { mutableStateOf("") }
    var resetToken by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message1 by remember { mutableStateOf("") }
    var message2 by remember { mutableStateOf("") }
    var message3 by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sekcja Kroku 1: Podanie Emaila
        StepContent(
            stepNumber = 1,
            currentStep = currentStep,
            title = "1. Podaj adres email",
        ) {
            TextField(
                value = email,
                onValueChange = { email = it;message1="" },
                label = { Text("Adres email") },
                enabled = currentStep == 1,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()){
                Spacer(modifier = Modifier.weight(0.5f))
                Button(
                    onClick = {
                        try{
                            val response = RetrofitInstance.api.forgotPassword(email = email)
                            response.enqueue(
                                CallbackHandler(
                                    onSuccess = { responseBody ->
                                        message1 = "Sukces! Sprawdź pocztę email"
                                        currentStep = 2
                                    },
                                    onError = {code, errorBody ->
                                        if(code==204){
                                            message1 = "Sukces! Sprawdź pocztę email"
                                            currentStep = 2
                                        }else{
                                            message1 = "Błąd serwera, spróbuj ponownie"
                                            println("\t* Błąd: $code")
                                            println("\t* Treść błędu: $errorBody")}
                                        }
                                        ,
                                    onFailure = {throwable ->
                                        println("\t* Request failed: ${throwable.message}")
                                        message1 = "Odrzucono zapytanie"
                                    }
                                )
                            )
                        }catch (e: Exception){
                            println("\t* Error: ${e.message}")
                        }
                              },
                    enabled = currentStep == 1,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text("Wyślij token")
                }
            }
            Text(message1)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sekcja Kroku 2: Wprowadzenie tokena
        StepContent(
            stepNumber = 2,
            currentStep = currentStep,
            title = "2. Wprowadź token"
        ) {
            TextField(
                value = resetToken,
                onValueChange = { resetToken = it;message2="" },
                label = { Text("Token resetu") },
                enabled = currentStep == 2,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()){
                Spacer(modifier = Modifier.weight(0.5f))
                Button(
                    onClick = {
                        try{
                            val response = RetrofitInstance.api.getResetPassword(token = resetToken)
                            response.enqueue(
                                CallbackHandler(
                                    onSuccess = { responseBody ->
                                        message2 = "Wprowadzono token"
                                        currentStep = 3
                                    },
                                    onError = {code, errorBody ->
                                        if(code==204){
                                            message2 = "Wprowadzono token"
                                            currentStep = 2
                                        }else{
                                            if(code == 404){
                                                message2 = "Błędny token"
                                            }
                                            println("\t* Błąd: $code")
                                            println("\t* Treść błędu: $errorBody")}
                                    },
                                    onFailure = {throwable ->
                                        println("\t* Request failed: ${throwable.message}")}
                                )
                            )
                        }catch (e: Exception){
                            println("\t* Error: ${e.message}")
                        }
                              },
                    enabled = currentStep == 2,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text("Kontynuuj")
                }
            }
            Text(message2)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sekcja Kroku 3: Ustawienie nowego hasła
        StepContent(
            stepNumber = 3,
            currentStep = currentStep,
            title = "3. Ustaw nowe hasło"
        ) {
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Nowe hasło") },
                visualTransformation = PasswordVisualTransformation(),
                enabled = currentStep == 3,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it;message3="" },
                label = { Text("Potwierdź nowe hasło") },
                visualTransformation = PasswordVisualTransformation(),
                enabled = currentStep == 3,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()){
                Spacer(modifier = Modifier.weight(0.5f))
                Button(
                    onClick = {
                        if(password!=confirmPassword){
                            message3 = "Hasła są różne"
                        }else {
                            try {
                                val response = RetrofitInstance.api.postResetPassword(
                                    token = resetToken,
                                    newPassword = password
                                )
                                response.enqueue(
                                    CallbackHandler(
                                        onSuccess = { responseBody ->
                                            navigateToScreen("loginscreen", navController)
                                            Toast.makeText(context,"Pomyślnie zmieniono hasło",Toast.LENGTH_LONG).show()
                                        },
                                        onError = { code, errorBody ->
                                            if(code==204){
                                                navigateToScreen("loginscreen", navController)
                                            }else {
                                                println("\t* Błąd: $code")
                                                println("\t* Treść błędu: $errorBody")
                                            }
                                                  },
                                        onFailure = { throwable ->
                                            println("\t* Request failed: ${throwable.message}")
                                        }
                                    )
                                )
                            } catch (e: Exception) {
                                println("\t* Error: ${e.message}")
                            }
                        }
                              },
                    enabled = currentStep == 3,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text("Zatwierdź")
                }
            }
            Text(message3)
        }
    }
}

@Composable
fun StepContent(
    stepNumber: Int,
    currentStep: Int,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .then(
                if (stepNumber > currentStep) Modifier.alpha(0.5f) else Modifier
            ),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom= 8.dp)
        )
        content()
    }
}
