package com.example.restaurantmanagementapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RequestClasses.CouponServer
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CouponsViewModel: ViewModel() {
    var coupons by mutableStateOf<List<CouponServer>?>(null)
    var selectedCoupon by mutableStateOf<CouponServer?>( null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchCoupons(customerId:Int,token:String,onComplete:() ->Unit){
        isLoading = true
        errorMessage = null

        viewModelScope.launch(Dispatchers.IO){
            println("Pobieram kupony:")
            try{
                val response = RetrofitInstance.api.getCustomerCoupons(customerId,"Bearer $token")
                response.enqueue(
                    CallbackHandler(
                        onSuccess = { responseBody ->
                            try{
                                val gson = Gson()
                                val objectType = object : TypeToken<List<CouponServer>>() {}.type
                                coupons = gson.fromJson(responseBody,objectType)
                                coupons?.forEach{ coupon->
                                    println("\t* Coupon ${coupon.id}: code ${coupon.code}")
                                }
                                isLoading = false
                                onComplete()
                            }catch (e:Exception){
                                errorMessage = "Error: ${e.message}"
                                println("\t* Parsing error: ${e.message}")
                                coupons = emptyList()
                            }
                        },
                        onError = {code, errorBody ->
                            println("\t* Błąd: $code")
                            println("\t* Treść błędu: $errorBody")},
                        onFailure = {throwable ->
                            println("\t* Request failed: ${throwable.message}")}
                    )
                )
            }catch (e: Exception){
                errorMessage = "\t* Error: ${e.message}"
                println(errorMessage)
            }
        }
    }

    fun selectCoupon(code:String){
        val tcoupon = coupons?.find{ coupon-> coupon.code.lowercase()==code.lowercase()}
        selectedCoupon = tcoupon
    }

}