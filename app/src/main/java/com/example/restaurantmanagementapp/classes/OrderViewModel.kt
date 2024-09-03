package com.example.restaurantmanagementapp.classes

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

class OrderViewModel : ViewModel() {
    private val _orderItems = mutableStateListOf<Meal>()
    val orderItems: SnapshotStateList<Meal> = _orderItems

    fun addToOrder(meal: Meal) {
        val tmeal = _orderItems.find{item -> item.id==meal.id}
        if(tmeal!=null){
            updateQuantity(_orderItems.indexOf(tmeal),tmeal.quantity+1)
        }else{
            _orderItems.add(meal)
        }

    }
    //TODO: Przy usuwaniu z koszyka, jeśli usuwa się produkt, to produkt pod nim przyjmuje jego quantity, dodatkowo nie jest aktualizowana cena jako cena*quantity
    fun deleteFromOrder(meal:Meal){
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
    fun getOrderTotal(): Double {
        return _orderItems.sumOf { it.price * it.quantity }
    }
    fun getSize(): Int{
        return _orderItems.sumOf{it.quantity}
    }

    fun updateQuantity(index:Int,value:Int){
        _orderItems[index].quantity = value
    }

}