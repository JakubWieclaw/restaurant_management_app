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
import com.example.restaurantmanagementapp.apithings.schemasclasses.CustomerServer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthViewModel() : ViewModel() {
    var customerData by mutableStateOf<LoginResponse?>(null)
    var phone by mutableStateOf<String?>(null)
    var isLogged: Boolean = false
    private val Context.dataStore by preferencesDataStore(name = "user_preferences")
    private val USERNAME_KEY = stringPreferencesKey("username")
    private val PASSWORD_KEY = stringPreferencesKey("password")
    private lateinit var context: Context

    fun setContext(context: Context) {
        this.context = context
    }

    fun loginFromSavedData(onComplete: (String, Int) -> Unit):Boolean{
        val username: String?
        val password: String?
        runBlocking {
            val preferences = context.dataStore.data.first()
            username = preferences[USERNAME_KEY]
            password = preferences[PASSWORD_KEY]
        }
        if(username!=null&&password!=null){
            val loginRequest = LoginRequest(username,password)
            login(loginRequest,true,onComplete={token,id -> onComplete(token,id)})
            return true
        }
        return false
    }

    fun login(loginRequest:LoginRequest,dontlogout:Boolean,onComplete:(String,Int)->Unit) {
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
                        getCustomerData(response.customerId,response.token)
                        onComplete(customerData!!.token,customerData!!.customerId)
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

    //Only for phone number :P
    fun getCustomerData(customerId:Int, customerToken:String){
        //TODO: Poprawić customer id
        //TODO: fetchowanie kuponów po zalogowaniu
        val call = RetrofitInstance.api.getCustomerData(customerId,"Bearer $customerToken")
        call.enqueue(
            CallbackHandler(
                onSuccess = { responseBody ->
                    println("Odpowiedź: $responseBody")
                    try {
                        val gson = Gson()
                        val responseType = object : TypeToken<CustomerServer>() {}.type
                        val response: CustomerServer = gson.fromJson(responseBody, responseType)
                        phone = response.phone
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
        //TODO: Poprawić, bo crashuje gdy customerData staje się null a gdzieś jest wymagane(np. w ustawieniach) jako !!
        //customerData = null
        isLogged = false
    }

    fun updateCustomer(registerRequest: RegisterRequest){
        val oldCustomerData = customerData
        runBlocking {
            val preferences = context.dataStore.data.first()
            //TODO: Może wystąpić problem dla użytkownika nie zapamiętującego danych logowania, a próbującego zmienić swoje dane
            registerRequest.password = preferences[PASSWORD_KEY]!!
        }
        val call = RetrofitInstance.api.updateCustomerData(customerData!!.customerId,registerRequest,"Bearer ${customerData!!.token}")
        call.enqueue(
            CallbackHandler(
                onSuccess = { responseBody ->
                    println("Odpowiedź: $responseBody")
                    customerData!!.customerName = registerRequest.name
                    customerData!!.customerSurname = registerRequest.surname
                    customerData!!.customerEmail = registerRequest.email
                    phone = registerRequest.phone
                },
                onError = {code, errorBody ->
                    println("Błąd: $code")
                    println("Treść błędu: $errorBody")
                    customerData = oldCustomerData
                },
                onFailure = {throwable ->
                    println("Request failed: ${throwable.message}")
                    customerData = oldCustomerData
                }
            )
        )
    }

    fun register(registerRequest: RegisterRequest,onComplete: (String, Int) -> Unit){
        val call = RetrofitInstance.api.register(registerRequest)
        call.enqueue(
            CallbackHandler(
                onSuccess = { responseBody ->
                    println("Odpowiedź: $responseBody")
                    try {
//                        val gson = Gson()
//                        val responseType = object : TypeToken<RequestResponse>() {}.type
//                        val response: RequestResponse = gson.fromJson(responseBody, responseType)
                        login(LoginRequest(registerRequest.email,registerRequest.password),false,onComplete = {token,id -> onComplete(token,id)})
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