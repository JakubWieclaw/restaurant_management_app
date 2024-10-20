package com.example.restaurantmanagementapp.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.restaurantmanagementapp.classes.Meal

class FavMealsViewModel:ViewModel() {
        private val _favItems = mutableStateListOf<Meal>()
        val favItems: SnapshotStateList<Meal> = _favItems

        fun addToFav(meal: Meal){
            val tmpMeal = _favItems.find{item -> item.id==meal.id}
            if(tmpMeal==null){
                _favItems.add(meal)
            }
        }
        fun removeFromFav(meal: Meal){
            val tmpMeal = _favItems.find{item -> item.id==meal.id}
                if(tmpMeal!=null){
                    _favItems.remove(meal)
                }
        }

        fun findMeal(meal: Meal):Boolean{
            val tmpMeal = _favItems.find{item -> item.id==meal.id}
            return tmpMeal!=null
        }
}