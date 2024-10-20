package com.example.restaurantmanagementapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RequestClasses.CouponServer
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.apithings.schemasclasses.Order
import com.example.restaurantmanagementapp.apithings.schemasclasses.OrderAddCommand
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderHistoryViewModel: ViewModel() {
    var orderHistory by mutableStateOf<List<Order>>(emptyList())
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchOrderHistory(customerId:Int,onComplete:() ->Unit){
        isLoading = true
        errorMessage = null

        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = RetrofitInstance.api.getCustomerOrders(customerId)
                response.enqueue(
                    CallbackHandler(
                        onSuccess = { responseBody ->
                            println("Odpowiedz: $responseBody")
                            try{
                                val gson = Gson()
                                val objectType = object : TypeToken<List<Order>>() {}.type
                                orderHistory = gson.fromJson(responseBody,objectType)
                                orderHistory?.forEach{ order->
                                    println("Order ${order.id}: price ${order.orderPrice}")
                                }
                                isLoading = false
                                onComplete()
                            }catch (e:Exception){
                                errorMessage = "Error: ${e.message}"
                                println("Parsing error: ${e.message}")
                                orderHistory = emptyList()
                            }
                        },
                        onError = {code, errorBody ->
                            println("Błąd: $code")
                            println("Treść błędu: $errorBody")},
                        onFailure = {throwable ->
                            println("Request failed: ${throwable.message}")}
                    )
                )
            }catch (e: Exception){
                errorMessage = "Error: ${e.message}"
                println(errorMessage)
            }
        }
    }

}