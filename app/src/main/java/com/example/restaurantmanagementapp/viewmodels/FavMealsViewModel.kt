package com.example.restaurantmanagementapp.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanagementapp.classes.Meal
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
class FavMealsViewModel:ViewModel() {
    private val _favItems = mutableStateListOf<Meal>()
    val favItems: SnapshotStateList<Meal> = _favItems
    private val Context.dataStore by preferencesDataStore(name = "user_favmeals")
    private val FAVMEALS_KEY = stringPreferencesKey("favourite_meals")
    private lateinit var context: Context

    fun setContext(context: Context) {
        this.context = context
    }

    fun saveIds() {
        val mealIds = _favItems.map{meal -> meal.id}
        val joinstring = mealIds.joinToString(",")
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[FAVMEALS_KEY] = joinstring
            }
        }
    }

    fun loadFavMeals(meals:List<Meal>) {
        val favMealIdsString: String?
        runBlocking {
            val preferences = context.dataStore.data.first()
            favMealIdsString = preferences[FAVMEALS_KEY] ?: ""
        }
        val favMealIds = if (!favMealIdsString.isNullOrEmpty()){
            favMealIdsString.split(",").map{it.toInt()}
        }else{
            emptyList()
        }
        _favItems.clear()
        for(meal in meals){
            if(meal.id in favMealIds){
                _favItems.add(meal)
            }
        }
    }

//    fun deleteIds() {
//        viewModelScope.launch {
//            context.dataStore.edit { preferences ->
//                preferences[FAVMEALS_KEY].clear()
//            }
//        }
//    }

    fun addToFav(meal: Meal){
        val tmpMeal = _favItems.find{item -> item.id==meal.id}
        if(tmpMeal==null){
            _favItems.add(meal)
        }
        saveIds()
    }
    fun removeFromFav(meal: Meal){
        val tmpMeal = _favItems.find{item -> item.id==meal.id}
            if(tmpMeal!=null){
                _favItems.remove(meal)
            }
        saveIds()
    }

    fun findMeal(meal: Meal):Boolean{
        val tmpMeal = _favItems.find{item -> item.id==meal.id}
        return tmpMeal!=null
    }
}