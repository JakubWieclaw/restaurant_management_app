package com.example.restaurantmanagementapp.CartScreen

import android.view.View
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.apithings.schemasclasses.MealQuantity
import com.example.restaurantmanagementapp.apithings.schemasclasses.OrderAddCommand
import com.example.restaurantmanagementapp.apithings.schemasclasses.UnwantedIngredient
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import org.json.JSONObject

@Composable
fun PaymentConfirm(
    orderViewModel: OrderViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
){
    val paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)
    val context = LocalContext.current
    var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(context){
        try {
            val mealQuantities = orderViewModel.orderItems.map { meal->
                MealQuantity(mealId = meal.id, quantity = meal.quantity)
            }
            val unwantedIngredients = orderViewModel.orderItems.mapIndexedNotNull { index, meal ->
                if(meal.removedIngredients.isNotEmpty()){
                    UnwantedIngredient(mealIndex = index, ingredients = meal.removedIngredients)
                }else{
                    null
                }
            }

            val orderAddCommand = OrderAddCommand(
                mealIds = mealQuantities,
                customerId = authViewModel.customerData!!.customerId,
                type = "NA_MIEJSCU",
                status = "GOTOWE",
                unwantedIngredients = unwantedIngredients,
                deliveryAddress = "",
                deliveryDistance = 0.0,
                tableId = "",
                people = 0,
                minutesForReservation = 120,
                couponCode = null
            )

            val stripeMock = stripeServerMock()
            stripeMock.go(object : stripeServerMock.Callback<String> {
                override fun onSuccess(result: String) {

                    val jsonObject = JSONObject(result)
                    paymentIntentClientSecret = jsonObject.getString("paymentIntent")
                    val publishableKey = jsonObject.getString("publishableKey")
                    PaymentConfiguration.init(context, publishableKey)
                    // Możesz teraz użyć paymentIntent i publishableKey w swojej aplikacji
                }

                override fun onError(e: Exception) {

                }
            })

//            val call = RetrofitInstance.api.addNewOrder(orderAddCommand, "Bearer ${authViewModel.customerData!!.token}")
//            call.enqueue(
//                CallbackHandler(
//                    onSuccess = { responseBody ->
//                        println("Odpowiedź: $responseBody")
//                        val responseJson = JSONObject(responseBody)
//                        paymentIntentClientSecret = responseJson.getString("paymentIntentClientSecret")
//                        println(paymentIntentClientSecret)
//                        val publishableKey = "pk_test_51Q4Qqx6w25OikflfELAfyRffrHpVJKl52TXAThc0QPyBccecIMGSZWK6No7HI0bZEkN8rHGgmkYFrSXbAiHL6AZ000pFpLF7Rz"
//                        PaymentConfiguration.init(context, publishableKey)
//                    },
//                    onError = { code, errorBody ->
//                        println("Błąd: $code")
//                        println("Treść błędu: $errorBody")
//                    },
//                    onFailure = { throwable ->
//                        println("Request failed: ${throwable.message}")
//                    }
//                )
//            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Button(
        onClick = {
            val currentClientSecret = paymentIntentClientSecret
            if (currentClientSecret != null) {
                presentPaymentSheet(paymentSheet, currentClientSecret)
            }
        }
    ){
        Text("Checkout")
    }

}

private fun presentPaymentSheet(
    paymentSheet: PaymentSheet,
    //customerConfig: PaymentSheet.CustomerConfiguration,
    paymentIntentClientSecret: String
) {
    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration(merchantDisplayName = "My merchant name")
    )
}

private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
    when(paymentSheetResult) {
        is PaymentSheetResult.Canceled -> {
            println("Canceled")
        }
        is PaymentSheetResult.Failed -> {
            println("Error: ${paymentSheetResult.error}")
        }
        is PaymentSheetResult.Completed -> {
            // Display for example, an order confirmation screen
            println("Completed")
        }
    }
}