package com.example.restaurantmanagementapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RequestClasses.Category
import com.example.restaurantmanagementapp.apithings.RequestClasses.LoginRequest
import com.example.restaurantmanagementapp.apithings.RequestClasses.RegisterRequest
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.apithings.schemasclasses.LoginResponse
import com.example.restaurantmanagementapp.apithings.schemasclasses.RequestResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AuthViewModel() : ViewModel() {
    var customerData by mutableStateOf<LoginResponse?>(null)
    var isLogged: Boolean = false

    fun login(loginRequest:LoginRequest) {
        val call = RetrofitInstance.api.login(loginRequest)
        call.enqueue(
            CallbackHandler(
                onSuccess = { responseBody ->
                    println("Odpowiedź: $responseBody")
                    try {
                        val gson = Gson()
                        val responseType = object : TypeToken<LoginResponse>() {}.type
                        val response: LoginResponse = gson.fromJson(responseBody, responseType)
                        customerData = response
                        isLogged = true
                    } catch (e: Exception) {
                        println("Parsing error: ${e.message}")
                    } },
                onError = {code, errorBody ->
                    println("Błąd: $code")
                    println("Treść błędu: $errorBody")
                },
                onFailure = {throwable ->
                    println("Request failed: ${throwable.message}")
                }
            )
        )
    }

    fun logout() {
        customerData = null
        isLogged = false
    }

    fun register(registerRequest: RegisterRequest){
        val call = RetrofitInstance.api.register(registerRequest)
        call.enqueue(
            CallbackHandler(
                onSuccess = { responseBody ->
                    println("Odpowiedź: $responseBody")
                    try {
//                        val gson = Gson()
//                        val responseType = object : TypeToken<RequestResponse>() {}.type
//                        val response: RequestResponse = gson.fromJson(responseBody, responseType)
                        login(LoginRequest(registerRequest.email,registerRequest.password))
                    } catch (e: Exception) {
                        println("Parsing error: ${e.message}")
                    } },
                onError = {code, errorBody ->
                    println("Błąd: $code")
                    println("Treść błędu: $errorBody")
                },
                onFailure = {throwable ->
                    println("Request failed: ${throwable.message}")
                }
            )
        )
    }


}