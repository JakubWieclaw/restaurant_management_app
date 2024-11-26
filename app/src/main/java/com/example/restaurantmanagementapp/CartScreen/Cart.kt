package com.example.restaurantmanagementapp.CartScreen

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.CustomBackground
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.apithings.schemasclasses.MealQuantity
import com.example.restaurantmanagementapp.apithings.schemasclasses.OrderAddCommand
import com.example.restaurantmanagementapp.apithings.schemasclasses.UnwantedIngredient
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.ui.theme.light_onPrimary
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel
import com.example.restaurantmanagementapp.viewmodels.CouponsViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.rememberPaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject

/*TODO: W android studio na urządzeniu aplikacja się crashuje przy próbie dokonania płatności (jakiś
    problem z getResource dot. animacji?
    W przypadku użycia na rzeczywistym telefonie działa poprawnie
*/

/*
* TODO: stworzyć nowy screen z już wypełnionymi niezmienialnymi danymi, gdzie paymentinit wywyluje sie qw launcheffect
*  ,wtedy może zadziala jak wszystko bedzie czyste, bo juz * idzie dostać od implementacji tego stripe'a
* */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(orderViewModel: OrderViewModel, couponsViewModel: CouponsViewModel, authViewModel: AuthViewModel,navController:NavController) {

    var selectedCoupon by remember { mutableStateOf(couponsViewModel.selectedCoupon)}
    var promoCode by remember { mutableStateOf(if(selectedCoupon!=null)selectedCoupon!!.code else "") }
    val paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)


    //var initcomplete by remember{mutableStateOf(false)}
    val context = LocalContext.current
    //var customerConfig by remember { mutableStateOf<PaymentSheet.CustomerConfiguration?>(null) }
    var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }

    val stripeServerMockInstance = remember { stripeServerMock() }

    val sheetState = rememberBottomSheetScaffoldState(bottomSheetState = SheetState(initialValue = SheetValue.Hidden, skipPartiallyExpanded = false))
    val scope = rememberCoroutineScope()
    var selectedMealIdx by remember { mutableStateOf<Int?>(null) }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            MealEditSheet(orderViewModel,selectedMealIdx,scope,sheetState)
        },
        sheetPeekHeight = 0.dp,
        containerColor = Color.Transparent
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(text = stringResource(id = R.string.cart_heading), style = Typography.titleLarge )
            Divider(
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            // MealCartCard list
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(orderViewModel.orderItems) { index, cartItem ->
                    MealCartCard(
                        orderViewModel = orderViewModel,
                        couponsViewModel = couponsViewModel,
                        index = index,
                        onEditClick = {selectedMealIdx = index;scope.launch { sheetState.bottomSheetState.expand()};println("Klik!")},
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Promo code section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = promoCode,
                    onValueChange = {promoCode = it; selectedCoupon = null },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp, end = 8.dp)
                        .background(color = Color.LightGray, shape = MaterialTheme.shapes.small),
                    textStyle = TextStyle(fontSize = 18.sp)
                )
                Button(
                    onClick = {
                        couponsViewModel.selectCoupon(promoCode)
                    },
                    modifier = Modifier
                        .background(color = Color.Yellow)
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 16.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 16.dp
                            )
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                ) {
                    Text(text = stringResource(id = R.string.coupon_apply), style = Typography.labelLarge)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cart summary
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                val itemprice = orderViewModel.getOrderTotal()
                val addons = 0.0
                val discount = itemprice - orderViewModel.getOrderTotal(mealId = couponsViewModel.selectedCoupon?.meal?.id, discount = couponsViewModel.selectedCoupon?.discountPercentage)

                CartSummaryItem(label = "Item Price", price = itemprice)
                CartSummaryItem(label = "Addons", price = addons)
                Divider(
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                CartSummaryItem(label = "Subtotal", price = itemprice + addons)
                CartSummaryItem(label = "Discount", price = -discount)
                Divider(
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                CartSummaryItem(
                    label = "Total",
                    price = itemprice + addons - discount,
                    isTotal = true
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
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
//                        //DEBUG
//                        val stripeMock = stripeServerMock()
//                        stripeMock.go(object : stripeServerMock.Callback<String> {
//                            override fun onSuccess(result: String) {
//
//                                val jsonObject = JSONObject(result)
//                                paymentIntentClientSecret = jsonObject.getString("paymentIntent")
//                                val publishableKey = jsonObject.getString("publishableKey")
//                                PaymentConfiguration.init(context, publishableKey)
//                                paymentIntentClientSecret?.let { presentPaymentSheet(paymentSheet, it) }
//                                // Możesz teraz użyć paymentIntent i publishableKey w swojej aplikacji
//                            }
//
//                            override fun onError(e: Exception) {
//
//                            }
//                        })

            val call = RetrofitInstance.api.addNewOrder(orderAddCommand, "Bearer ${authViewModel.customerData!!.token}")
            call.enqueue(
                CallbackHandler(
                    onSuccess = { responseBody ->
                        println("Odpowiedź: $responseBody")
                        val responseJson = JSONObject(responseBody)
                        paymentIntentClientSecret = responseJson.getString("paymentIntentClientSecret")
                        println(paymentIntentClientSecret)
                        val publishableKey = "pk_test_51Q4Qqx6w25OikflfELAfyRffrHpVJKl52TXAThc0QPyBccecIMGSZWK6No7HI0bZEkN8rHGgmkYFrSXbAiHL6AZ000pFpLF7Rz"

                        PaymentConfiguration.init(context, publishableKey)
                        paymentIntentClientSecret?.let { presentPaymentSheet(paymentSheet, it) }
                    },
                    onError = { code, errorBody ->
                        println("Błąd: $code")
                        println("Treść błędu: $errorBody")
                    },
                    onFailure = { throwable ->
                        println("Request failed: ${throwable.message}")
                    }
                )
            )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.gotopayment), style = Typography.labelLarge, color = Color.White)
            }
        }
    }

}
@Composable
fun CartSummaryItem(label: String, price: Double, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 20.sp else 16.sp,
            color = if (isTotal) Color.Black else Color.Gray
        )
        Text(
            text = "${String.format("%.2f", price)} zł",
            fontSize = if (isTotal) 20.sp else 16.sp,
            color = if (isTotal) Color.Black else Color.Gray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealEditSheet(orderViewModel: OrderViewModel, index:Int?, scope:CoroutineScope, sheetState: BottomSheetScaffoldState ){
    if(index!=null && index < orderViewModel.orderItems.size) {
        val tmeal = orderViewModel.orderItems[index]
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.mealeditheader) +" "+ tmeal.name,style = Typography.titleSmall , modifier = Modifier.padding(bottom = 16.dp))
            tmeal.ingredients.forEach { ingredient ->
                val isRemoved = tmeal.removedIngredients.find { item -> item == ingredient } == ingredient
                Row(modifier = Modifier.fillMaxWidth().padding(vertical=10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "- $ingredient", textDecoration = if(isRemoved) TextDecoration.LineThrough else TextDecoration.None, style= Typography.labelMedium)
                    Row(){
                        IconButton(
                            onClick = { orderViewModel.addIngredient(index, ingredient) },
                            enabled = isRemoved,
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = if (isRemoved) Color.Green else Color.Gray,
                                    shape = RoundedCornerShape(50)
                                )
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        IconButton(
                            onClick = { orderViewModel.removeIngredient(index, ingredient) },
                            enabled = !isRemoved,
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = if (!isRemoved) Color.Red else Color.Gray,
                                    shape = RoundedCornerShape(50)
                                )
                        ) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
                }
            }
        }
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