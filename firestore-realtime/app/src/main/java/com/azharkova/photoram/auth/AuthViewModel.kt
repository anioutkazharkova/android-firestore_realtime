package com.azharkova.photoram.auth

import androidx.lifecycle.ViewModel
import com.azharkova.photoram.BaseViewModel
import com.azharkova.photoram.FirebaseAuthHelper
import com.azharkova.photoram.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : BaseViewModel(){
    val isLoggedIn = MutableStateFlow(false)

    fun login(email: String, password: String) {
        modelScope.launch {
          val result =  FirebaseAuthHelper.instance.loginUser(email,password)
          when (result) {
              is Result.Success<*> -> isLoggedIn.value = true
              else -> isLoggedIn.value = false
          }
        }
    }
}