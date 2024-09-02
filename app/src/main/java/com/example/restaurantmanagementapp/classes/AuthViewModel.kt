package com.example.restaurantmanagementapp.classes

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> get() = _token
    var isLogged: Boolean = false

    fun login(newToken: String) {
        _token.value = newToken
        isLogged = true
    }

    fun logout() {
        _token.value = null
        isLogged = false
    }
}