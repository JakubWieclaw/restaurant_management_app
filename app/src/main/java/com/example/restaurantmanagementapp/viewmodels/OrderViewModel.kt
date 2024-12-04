package com.example.restaurantmanagementapp.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.example.restaurantmanagementapp.apithings.CallbackHandler
import com.example.restaurantmanagementapp.apithings.RetrofitInstance
import com.example.restaurantmanagementapp.apithings.schemasclasses.MealQuantity
import com.example.restaurantmanagementapp.apithings.schemasclasses.OrderAddCommand
import com.example.restaurantmanagementapp.apithings.schemasclasses.UnwantedIngredient
import com.example.restaurantmanagementapp.apithings.schemasclasses.Meal

class OrderViewModel : ViewModel() {
    private val _orderItems = mutableStateListOf<Meal>()
    val orderItems: SnapshotStateList<Meal> = _orderItems

    fun addToOrder(meal: Meal) {
        val tmeal = _orderItems.find{it.id == meal.id && it.removedIngredients.toSet() == meal.removedIngredients.toSet()}
        if(tmeal!=null){
            println("tmeal: " +tmeal.removedIngredients.toSet())
            println("meal:"+ meal.removedIngredients.toSet())
            updateQuantity(_orderItems.indexOf(tmeal),tmeal.quantity+1)
        }else{
            val newMeal = meal.copy(
                removedIngredients = meal.removedIngredients.toMutableStateList()
            )
            _orderItems.add(newMeal)
        }

    }

    fun deleteFromOrder(meal: Meal){
        _orderItems.remove(meal)
    }
    fun removeFromOrder(meal: Meal) {
        val tmeal = _orderItems.find{item -> item.id==meal.id}
        if(tmeal!=null){
            if(tmeal.quantity>1)
                updateQuantity(_orderItems.indexOf(tmeal),tmeal.quantity-1)
            else{
                _orderItems.remove(meal)
            }
        }
    }
    fun clearOrder() {
        _orderItems.clear()
    }
    fun getOrderTotal(mealId:Int? =null,discount:Double? = null): Double {
        return if(mealId!=null&&discount!=null){
            _orderItems.sumOf {if(it.id==mealId) (1.0-discount/100.0) * it.price * it.quantity else  it.price * it.quantity}
        }else{
            _orderItems.sumOf {it.price * it.quantity }
        }

    }
    fun getSize(): Int{
        return _orderItems.sumOf{it.quantity}
    }

    fun updateQuantity(index:Int,value:Int){
        _orderItems[index].quantity = value.coerceAtLeast(1).coerceAtMost(999)
    }

    fun removeIngredient(index: Int, ingredient:String){
//        val tmeal = _orderItems.find{item -> item.id==meal.id}
//        if(tmeal!=null){
//            tmeal.removedIngredients.add(ingredient)
//        }
        _orderItems[index].removedIngredients.add(ingredient)
    }

    fun addIngredient(index: Int, ingredient:String){
//        val tmeal = _orderItems.find{item -> item.id==meal.id}
//        if(tmeal!=null){
//            tmeal.removedIngredients.remove(ingredient)
//        }
        _orderItems[index].removedIngredients.remove(ingredient)
    }

    //TODO: Sprawdzić potem czy działa
    fun finalizeOrder(customerId:Int,customerToken:String, orderType:String, orderStatus:String, deliveryAddress:String, deliveryDistance:Double){
        val mealQuantities = orderItems.map { meal->
            MealQuantity(mealId = meal.id, quantity = meal.quantity)
        }
        val unwantedIngredients = orderItems.mapIndexedNotNull { index, meal ->
            if(meal.removedIngredients.isNotEmpty()){
                UnwantedIngredient(mealIndex = index, ingredients = meal.removedIngredients)
            }else{
                null
            }
        }

        val orderAddCommand = OrderAddCommand(
            mealIds = mealQuantities,
            customerId = customerId,
            type = orderType,
            status = orderStatus,
            unwantedIngredients = unwantedIngredients,
            deliveryAddress = deliveryAddress,
            deliveryDistance = deliveryDistance,

            tableId = "",
            people = 0,
            minutesForReservation = 0,
            couponCode = null
        )

        println(customerToken)
        val call = RetrofitInstance.api.addNewOrder(orderAddCommand, "Bearer $customerToken")
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
    }

}