package com.azharkova.photoram.splash

import com.azharkova.photoram.BaseViewModel
import com.azharkova.photoram.FirebaseAuthHelper
import kotlinx.coroutines.flow.MutableStateFlow

class SplashViewModel : BaseViewModel() {
    val isLoggedIn = MutableStateFlow(false)

    fun checkAuth() {
        FirebaseAuthHelper.instance.check {
            isLoggedIn.value = it
        }
    }
}