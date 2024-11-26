package com.example.restaurantmanagementapp.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantmanagementapp.TestData
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RequestClasses.Category
import com.example.restaurantmanagementapp.apithings.RequestClasses.LoginRequest
import com.example.restaurantmanagementapp.apithings.RequestClasses.RegisterRequest
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.apithings.schemasclasses.LoginResponse
import com.example.restaurantmanagementapp.apithings.schemasclasses.RequestResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthViewModel() : ViewModel() {
    var customerData by mutableStateOf<LoginResponse?>(null)
    var isLogged: Boolean = false
    private val Context.dataStore by preferencesDataStore(name = "user_preferences")
    private val USERNAME_KEY = stringPreferencesKey("username")
    private val PASSWORD_KEY = stringPreferencesKey("password")
    private lateinit var context: Context

    fun setContext(context: Context) {
        this.context = context
    }

    fun loginFromSavedData():Boolean{
        val username: String?
        val password: String?
        runBlocking {
            val preferences = context.dataStore.data.first()
            username = preferences[USERNAME_KEY]
            password = preferences[PASSWORD_KEY]
        }
        if(username!=null&&password!=null){
            val loginRequest = LoginRequest(username,password)
            login(loginRequest,true)
            return true
        }
        return false
    }

    fun login(loginRequest:LoginRequest,dontlogout:Boolean) {
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
                        if(dontlogout) {
                            viewModelScope.launch {
                                context.dataStore.edit { preferences ->
                                    preferences[USERNAME_KEY] = loginRequest.email
                                    preferences[PASSWORD_KEY] = loginRequest.password
                                }
                            }
                        }
                    } catch (e: Exception) {
                        println("Parsing error: ${e.message}")
                    }
                            },
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
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
        }
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
                        login(LoginRequest(registerRequest.email,registerRequest.password),false)
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