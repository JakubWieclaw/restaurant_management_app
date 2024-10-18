package com.example.restaurantmanagementapp.CartScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.classes.AuthViewModel
import com.example.restaurantmanagementapp.classes.OrderViewModel
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.Customer
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.rememberPaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

/*TODO: W android studio na urządzeniu aplikacja się crashuje przy próbie dokonania płatności (jakiś
    problem z getResource dot. animacji?
    W przypadku użycia na rzeczywistym telefonie działa poprawnie
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(orderViewModel: OrderViewModel, authViewModel: AuthViewModel) {
    var promoCode by remember { mutableStateOf("") }

    val paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)

    val context = LocalContext.current
    var customerConfig by remember { mutableStateOf<PaymentSheet.CustomerConfiguration?>(null) }
    var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }

    val mock2Instance = remember { mock2() }

    val sheetState = rememberBottomSheetScaffoldState(bottomSheetState = SheetState(initialValue = SheetValue.Hidden, skipPartiallyExpanded = false))
    val scope = rememberCoroutineScope()
    var selectedMealIdx by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) { // Trigger on composition
        // Call the go method and handle the result
        try {
            val responseJson0 = mock2Instance.go(object : mock2.Callback<String> {
                override fun onSuccess(result: String) {
                    val responseJson = JSONObject(result)

                    paymentIntentClientSecret = responseJson.getString("paymentIntent")
                    customerConfig = PaymentSheet.CustomerConfiguration(
                        id = responseJson.getString("customer"),
                        ephemeralKeySecret = responseJson.getString("ephemeralKey")
                    )
                    val publishableKey = responseJson.getString("publishableKey")
                    PaymentConfiguration.init(context, publishableKey)
                }

                override fun onError(e: Exception) {
                    // Handle the error here
                    e.printStackTrace()
                    // Optionally show an error message to the user
                }
            }) // Now a suspend function

        } catch (e: Exception) {
            // Handle any exceptions (optional)
            e.printStackTrace()
            // You may want to show an error message or handle the error gracefully
        }
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            mealEditSheet(orderViewModel,selectedMealIdx,scope,sheetState)
        },
        sheetPeekHeight = 0.dp
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(text = "Twój koszyk: ", modifier = Modifier.height(24.dp), fontSize = 20.sp)
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
                    onValueChange = { promoCode = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp, end = 8.dp)
                        .background(color = Color.LightGray, shape = MaterialTheme.shapes.small),
                    textStyle = TextStyle(fontSize = 18.sp)
                )
                Button(
                    onClick = { /* Handle apply promo code */ },
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
                    Text(text = "Apply", style = TextStyle(color = Color.Black), fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cart summary
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                val itemprice = orderViewModel.getOrderTotal()
                val addons = 20.0
                val discount = 20.0
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
                    val currentConfig = customerConfig
                    val currentClientSecret = paymentIntentClientSecret

                    if (currentConfig != null && currentClientSecret != null) {
                        presentPaymentSheet(paymentSheet, currentConfig, currentClientSecret)
                    }
                }
            ) {
                Text("Przejdź do płatności")
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
private fun presentPaymentSheet(
    paymentSheet: PaymentSheet,
    customerConfig: PaymentSheet.CustomerConfiguration,
    paymentIntentClientSecret: String
) {
    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration(
            merchantDisplayName = "My merchant name",
            customer = customerConfig,
            // Set `allowsDelayedPaymentMethods` to true if your business handles
            // delayed notification payment methods like US bank accounts.
            allowsDelayedPaymentMethods = true
        )
    )
}

private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
    when(paymentSheetResult) {
        is PaymentSheetResult.Canceled -> {
            print("Canceled")
        }
        is PaymentSheetResult.Failed -> {
            print("Error: ${paymentSheetResult.error}")
        }
        is PaymentSheetResult.Completed -> {
            // Display for example, an order confirmation screen
            print("Completed")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mealEditSheet(orderViewModel: OrderViewModel, index:Int?, scope:CoroutineScope, sheetState: BottomSheetScaffoldState ){
    if(index!=null && index < orderViewModel.orderItems.size) {
        val tmeal = orderViewModel.orderItems[index]
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            tmeal.ingredients.forEach { ingredient ->
                Row() {
                    IconButton(onClick = { scope.launch { sheetState.bottomSheetState.hide() } }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                    Text("- $ingredient")
                    IconButton(
                        onClick = { orderViewModel.addIngredient(index, ingredient) },
                        enabled = tmeal.removedIngredients.find { item -> item == ingredient } == ingredient
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    }
                    IconButton(
                        onClick = { orderViewModel.removeIngredient(index, ingredient) },
                        enabled = tmeal.removedIngredients.find { item -> item == ingredient } == null
                    ) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                    }
                }

            }
        }
    }
}