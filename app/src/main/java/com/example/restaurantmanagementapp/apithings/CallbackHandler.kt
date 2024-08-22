package com.example.restaurantmanagementapp.apithings
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class CallbackHandler(
    private val onSuccess: (String) -> Unit,
    private val onError: (Int, String?) -> Unit,
    private val onFailure: (Throwable) -> Unit
) : Callback<ResponseBody> {

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
        try {
            if (response.isSuccessful) {
                val responseBody = response.body()?.string()
                if (responseBody != null) {
                    onSuccess(responseBody)
                } else {
                    onError(response.code(), "Odpowiedź jest pusta")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                onError(response.code(), errorBody)
            }
        } catch (e: Exception) {
            onError(response.code(), "Nie można przetworzyć odpowiedzi: ${e.message}")
        }
    }

    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        onFailure(t)
    }
}