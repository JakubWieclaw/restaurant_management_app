package com.example.restaurantmanagementapp.apithings
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
//    //order-controller
//    @POST("api/orders/add")
//    fun addNewOrder(@Body request: NewOrderRequest): Call<ResponseBody>
//    @GET("api/orders/get/customer/{customerId}")
//    fun getCustomerOrders(@Path("customerId") customerId: Int): Call<ResponseBody>
//

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<ResponseBody>
    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<ResponseBody>

    @GET("api/categories/all")
    fun getCategories(): Call<ResponseBody>
    @GET("api/meals/all")
    fun getMeals(): Call<ResponseBody>
    @GET("api/opinions/average-rating/{mealId}")
    fun getAvgRating(@Path("mealId") mealId:Int): Call<ResponseBody>
    @GET("api/opinions/meal/{mealId}")
    fun getOpinionsForMeal(@Path("mealId") mealId:Int): Call<ResponseBody>
    @GET("api/photos/controller")
    fun getPhoto(): Call<ResponseBody>
    @GET("api/coupons/customer/{customerId}")
    fun getCustomerCoupons(@Path("customerId") customerId:Int): Call<ResponseBody>

}