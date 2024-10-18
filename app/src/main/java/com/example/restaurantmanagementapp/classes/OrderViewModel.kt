package com.example.restaurantmanagementapp.classes

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

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

}