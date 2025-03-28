package com.example.restaurantmanagementapp.CartScreen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.CustomBackground
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.UserPanelScreen.OrderCard
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.apithings.schemasclasses.MealQuantity
import com.example.restaurantmanagementapp.apithings.schemasclasses.OrderAddCommand
import com.example.restaurantmanagementapp.apithings.schemasclasses.UnwantedIngredient
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.ui.theme.light_background
import com.example.restaurantmanagementapp.ui.theme.light_onPrimary
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel
import com.example.restaurantmanagementapp.viewmodels.CouponsViewModel
import com.example.restaurantmanagementapp.viewmodels.HoursViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.rememberPaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(orderViewModel: OrderViewModel, couponsViewModel: CouponsViewModel, authViewModel: AuthViewModel,hoursViewModel: HoursViewModel, navController:NavController) {
    var onsiteOrDelivery by remember { mutableStateOf("NA_MIEJSCU")}
    var selectedCoupon by remember { mutableStateOf(couponsViewModel.selectedCoupon)}
    var promoCode by remember { mutableStateOf(selectedCoupon?.code) }
    var orderId by remember { mutableIntStateOf(-1)}
    val paymentSheet = rememberPaymentSheet {
            paymentSheetResult ->
        onPaymentSheetResult(paymentSheetResult, onComplete = {
            if(onsiteOrDelivery=="NA_MIEJSCU") {


                try {
                    val call = RetrofitInstance.api.addToReservation(
                        hoursViewModel.tableReservations[0].id,
                        orderId,
                        "Bearer ${authViewModel.customerData!!.token}"
                    )
                    call.enqueue(
                        CallbackHandler(
                            onSuccess = { responseBody ->
                                println("Odpowiedź: $responseBody")
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
            orderViewModel.clearOrder()
            promoCode=""
            couponsViewModel.selectedCoupon = null
        })
    }

    var promoCodeValid by remember{ mutableIntStateOf(0) }
    val promoTextFieldBorderColor = if(promoCodeValid==-1) Color.Red else if(promoCodeValid==0) Color.Transparent else Color.Green
    val context = LocalContext.current
    var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true )
    var showSheet by remember{ mutableStateOf(false)}
    var selectedMealIdx by remember { mutableStateOf<Int?>(null) }
    var address by remember { mutableStateOf("")}

    Scaffold(
        containerColor = Color.Transparent
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
        ) {
            Text(text = stringResource(id = R.string.cart_heading), style = Typography.titleLarge )
            Divider(
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            if(onsiteOrDelivery=="NA_MIEJSCU") {
                ReservationCard(
                    hoursViewModel,
                    onBtnClick = { navigateToScreen("tablereservation", navController) })
            }else{
                TextField(
                    label = {Text("Adres dostawy")} ,
                    value = address,
                    onValueChange = {address = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    textStyle = TextStyle(fontSize = 18.sp, color = Color.Black)
                )
            }
            Row(modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()){
                Button(modifier = Modifier.padding(horizontal = 4.dp).weight(0.5f), colors = if(onsiteOrDelivery=="NA_MIEJSCU") ButtonDefaults.buttonColors(Color.Green) else ButtonDefaults.buttonColors(), onClick = {onsiteOrDelivery="NA_MIEJSCU"}){
                    Text(text = stringResource(id = R.string.orderonsite))
                }
                Button(modifier = Modifier.padding(horizontal = 4.dp).weight(0.5f), colors = if(onsiteOrDelivery=="DOSTAWA") ButtonDefaults.buttonColors(Color.Green) else ButtonDefaults. buttonColors(), onClick = {onsiteOrDelivery="DOSTAWA"}){
                    Text(text = stringResource(id = R.string.orderdelivery))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            // MealCartCard list
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(orderViewModel.orderItems) { index, cartItem ->
                    MealCartCard(
                        orderViewModel = orderViewModel,
                        couponsViewModel = couponsViewModel,
                        index = index,
                        onEditClick = {selectedMealIdx = index;showSheet=true;println("Klik!")},
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
                    .border(
                        color = promoTextFieldBorderColor,
                        width = 1.5.dp,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {

                BasicTextField(
                    value = promoCode?: "",
                    onValueChange = {promoCode = it; selectedCoupon = null;promoCodeValid=0 },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp, end = 8.dp)
                        .background(color = Color.LightGray, shape = MaterialTheme.shapes.small),
                    textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                )
                Button(
                    onClick = {
                        couponsViewModel.selectCoupon(promoCode?:"")
                        couponsViewModel.validateCoupon(customerToken = authViewModel.customerData!!.token,onComplete={result->if(result) promoCodeValid=1 else promoCodeValid=-1})
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
                CartSummaryItem(
                    label = stringResource(id = R.string.totalSum),
                    price = itemprice + addons,
                    discount = discount,
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
                            type = onsiteOrDelivery,
                            status = "OCZEKUJĄCE",
                            unwantedIngredients = unwantedIngredients,
                            deliveryAddress = address,
                            //TODO: Do poprawy
                            deliveryDistance = if(onsiteOrDelivery=="NA_MIEJSCU") 0.0 else 5.0,
                            tableId = if(hoursViewModel.tableReservations.isNotEmpty()) hoursViewModel.tableReservations[0].tableId else "",
                            people = if(hoursViewModel.tableReservations.isNotEmpty()) hoursViewModel.tableReservations[0].people else 1,
                            minutesForReservation = 120,
                            couponCode = promoCode
                        )
            val call = RetrofitInstance.api.addNewOrder(orderAddCommand, "Bearer ${authViewModel.customerData!!.token}")
            call.enqueue(
                CallbackHandler(
                    onSuccess = { responseBody ->
                        println("Odpowiedź: $responseBody")
                        val responseJson = JSONObject(responseBody)
                        paymentIntentClientSecret = responseJson.getString("paymentIntentClientSecret")
                        println(paymentIntentClientSecret)
                        val publishableKey = "pk_test_51Q4Qqx6w25OikflfELAfyRffrHpVJKl52TXAThc0QPyBccecIMGSZWK6No7HI0bZEkN8rHGgmkYFrSXbAiHL6AZ000pFpLF7Rz"
                        orderId = responseJson.getInt("id")
                        PaymentConfiguration.init(context, publishableKey)
                        paymentIntentClientSecret?.let { presentPaymentSheet(paymentSheet, it) }
                    },
                    onError = { code, errorBody ->
                        orderId=-1
                        println("Błąd: $code")
                        println("Treść błędu: $errorBody")
                    },
                    onFailure = { throwable ->
                        orderId=-1
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

        if(showSheet){
            MealEditSheet(
                orderViewModel = orderViewModel,
                index = selectedMealIdx,
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            )
        }
    }


}

@Composable
fun ReservationCard(hoursViewModel: HoursViewModel, onBtnClick:()->Unit) {
    if(hoursViewModel.tableReservations.isNotEmpty()){
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(color = light_background, shape = RoundedCornerShape(16.dp))
                .border(2.dp, Color.Gray, shape = RoundedCornerShape(16.dp))
                .padding(10.dp)
                , horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(0.5f)){
                    Text(text = stringResource(id = R.string.reservation))
                    Text(text = hoursViewModel.tableReservations[0].day)
                }
                Column(modifier = Modifier.weight(0.5f)){
                    Text(text = stringResource(id = R.string.hour) + ": ${hoursViewModel.tableReservations[0].startTime.dropLast(3)}-${hoursViewModel.tableReservations[0].endTime.dropLast(3)}")
                    Text(text = stringResource(id = R.string.number_of_people) +": ${hoursViewModel.tableReservations[0].people}")
                }

            }
    }else{
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Zamówienie bez rezerwacji", softWrap = true, modifier = Modifier.weight(0.5f))
            Button(onClick = {onBtnClick()}, modifier = Modifier.weight(0.5f)){
                Text(text = stringResource(id = R.string.add_reservation), softWrap = false)
            }
        }
    }
}

@Composable
fun CartSummaryItem(label: String, price: Double, discount: Double, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 20.sp else 16.sp,
            color = if (isTotal) Color.Black else Color.Gray
        )
        Row(){
            Text(
                text = "${String.format("%.2f", price)} zł",
                fontSize = if (discount>0) 16.sp else 20.sp,
                color = if (discount>0) Color.Red else Color.Black,
                textDecoration = if(discount>0) TextDecoration.LineThrough else null
            )
            if(discount>0){
                Text(
                    text = " ${String.format("%.2f", price-discount)} zł",
                    fontSize = 20.sp,
                    color = Color.Black
                )
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

private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult, onComplete:() -> Unit) {
    when(paymentSheetResult) {
        is PaymentSheetResult.Canceled -> {
            println("Canceled")
        }
        is PaymentSheetResult.Failed -> {
            println("Error: ${paymentSheetResult.error}")
        }
        is PaymentSheetResult.Completed -> {
            println("Completed")
            onComplete()
        }
    }
}