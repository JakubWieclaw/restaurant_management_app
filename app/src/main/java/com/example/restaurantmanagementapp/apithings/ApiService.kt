package com.example.restaurantmanagementapp.apithings
import com.example.restaurantmanagementapp.apithings.RequestClasses.LoginRequest
import com.example.restaurantmanagementapp.apithings.RequestClasses.RegisterRequest
import com.example.restaurantmanagementapp.apithings.schemasclasses.OpinionAddCommand
import com.example.restaurantmanagementapp.apithings.schemasclasses.OrderAddCommand
import com.example.restaurantmanagementapp.apithings.schemasclasses.PossibleReservationHoursForDay
import com.example.restaurantmanagementapp.apithings.schemasclasses.ReservationRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    //order-controller
    @POST("api/orders/add")
    fun addNewOrder(@Body request: OrderAddCommand): Call<ResponseBody>
    @GET("api/orders/get/customer/{customerId}")
    fun getCustomerOrders(@Path("customerId") customerId: Int): Call<ResponseBody>

    //opinion-controller
    @POST("api/opinions/add")
    fun addOpinion(@Body request: OpinionAddCommand): Call<ResponseBody>
    @GET("api/opinions/average-rating/{mealId}")
    fun getAvgRating(@Path("mealId") mealId:Int): Call<ResponseBody>
    @GET("api/opinions/meal/{mealId}")
    fun getOpinionsForMeal(@Path("mealId") mealId:Int): Call<ResponseBody>

    //meal-controller
    @GET("api/meals/all")
    fun getMeals(): Call<ResponseBody>

    //coupon-controller
    @GET("api/coupons/customer/{customerId}")
    fun getCustomerCoupons(@Path("customerId") customerId:Int): Call<ResponseBody>
    //Niepotrzebne? chyba 'active' z kuponu wystarczy
//    @GET("api/coupons/validate")
//    fun getCouponValidate(): Call<ResponseBody>

    //categpry-controller
    @GET("api/categories/all")
    fun getCategories(): Call<ResponseBody>

    //auth-controller
    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<ResponseBody>
//    @GET("auth/password-reset")
//    fun getResetPassword(): Call<ResponseBody>
//    @POST("/auth/password-reset")
//    fun postResetPassword(@Body request: ResetPasswordRequest): Call<ResponseBody>
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<ResponseBody>
    @POST("/auth/forgot-password")
    fun forgotPassword(@Body email: String): Call<ResponseBody>

    //table-reservation-controller
    @POST("api/table-reservation/make")
    fun makeReservation(@Body request: ReservationRequest): Call<ResponseBody>
    @POST("api/table-reservation/available-hours/day")
    fun availableHours(@Body request: PossibleReservationHoursForDay): Call<ResponseBody>

    //photo-controller
    @GET("api/photos/download")
    fun getPhoto(): Call<ResponseBody>

    //customer-controller
    @GET("api/customer/get/{id}")
    fun getCustomerData(@Path("id") id:Int):Call<ResponseBody>
}