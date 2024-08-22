package com.example.restaurantmanagementapp.apithings
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
interface ApiService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<ResponseBody>
    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<ResponseBody>
}