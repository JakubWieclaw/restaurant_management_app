package com.example.restaurantmanagementapp.apithings
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            //Dla emulatorów
            .baseUrl("http://10.0.2.2:8080/")
            //dla urządzeń fizyczych
            //.baseUrl("http://127.0.0.1:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
