package com.example.restaurantmanagementapp.apithings
import com.example.restaurantmanagementapp.apithings.RequestClasses.LoginRequest
import com.example.restaurantmanagementapp.apithings.RequestClasses.RegisterRequest
import com.example.restaurantmanagementapp.apithings.schemasclasses.ContactFormCommand
import com.example.restaurantmanagementapp.apithings.schemasclasses.MakeReservationCommand
import com.example.restaurantmanagementapp.apithings.schemasclasses.OpinionAddCommand
import com.example.restaurantmanagementapp.apithings.schemasclasses.OrderAddCommand
import com.example.restaurantmanagementapp.apithings.schemasclasses.PossibleReservationHoursForDay
import com.example.restaurantmanagementapp.apithings.schemasclasses.ReservationRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //order-controller
    @POST("api/orders/add")
    fun addNewOrder(@Body request: OrderAddCommand, @Header("Authorization") token :String): Call<ResponseBody>
    @GET("api/orders/get/customer/{customerId}")
    fun getCustomerOrders(@Path("customerId") customerId: Int, @Header("Authorization") token :String): Call<ResponseBody>
    @POST("api/orders/add-to-reservation")
    fun addToReservation(
        @Query("reservationId") reservationId:Int,
        @Query("orderId") orderId:Int,
        @Header("Authorization") token :String
    ): Call<ResponseBody>

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

    //customer-controller
    @GET("api/customer/get/{id}")
    fun getCustomerData(@Path("id") id:Int, @Header("Authorization") token :String):Call<ResponseBody>
    @PUT("api/customer/update/{id}")
    fun updateCustomerData(@Path("id") id:Int, @Body request: RegisterRequest, @Header("Authorization") token :String):Call<ResponseBody>

    //coupon-controller
    @GET("api/coupons/customer/{customerId}")
    fun getCustomerCoupons(@Path("customerId") customerId:Int, @Header("Authorization") token :String): Call<ResponseBody>
    @GET("api/coupons/validate")
    fun getCouponValidate(@Query("code")code:String,@Query("customerId")customerId:Int,@Query("mealId")mealId:Int, @Header("Authorization") token :String): Call<ResponseBody>



    //categpry-controller
    @GET("api/categories/all")
    fun getCategories(): Call<ResponseBody>

    //auth-controller
    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<ResponseBody>
    @GET("auth/password-reset")
    fun getResetPassword(@Query("token") token:String ): Call<ResponseBody>
    @POST("auth/password-reset")
    fun postResetPassword(@Query("token") token:String, @Query("newPassword") newPassword:String): Call<ResponseBody>

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<ResponseBody>
    @POST("/auth/forgot-password")
    fun forgotPassword(@Query("email") email: String): Call<ResponseBody>

    //table-reservation-controller
    @GET("/api/reservations/available-hours/{day}")
    fun getAvailableHoursForDay(
        @Path("day") day:String,
        @Query("reservationDuration") reservationDuration:Int,
        @Query("minutesToAdd") minutesToAdd:Int,
        @Query("numberOfPeople") numberOfPeople:Int,
        @Header("Authorization") token :String
    ): Call<ResponseBody>
    @POST("/api/reservations")
    fun createReservation(
        @Body reservation: MakeReservationCommand,
        @Header("Authorization") token :String
    ): Call<ResponseBody>
    @DELETE("api/reservations/{id}")
    fun cancelReservation(@Path("id") id:Int, @Header("Authorization") token :String):Call<ResponseBody>
    @GET("api/reservations/customer/{customerId}")
    fun getReservations(@Path("customerId") customerId: Int, @Header("Authorization") token :String):Call<ResponseBody>

    //photo-controller
    @GET("api/photos/download")
    fun getPhoto(@Query("filename") filename:String): Call<ResponseBody>


    //contact-form-controller
    @POST("/api/contact-form/send")
    fun createContactForm(@Body contactForm: ContactFormCommand): Call<ResponseBody>
}