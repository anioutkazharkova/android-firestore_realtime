package com.azharkova.photoram.settings

import androidx.lifecycle.ViewModel
import com.azharkova.photoram.FirebaseAuthHelper
import com.azharkova.photoram.data.UserData
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewModel : ViewModel() {

    val userData: MutableStateFlow<UserData?> =  MutableStateFlow(null)

    fun loadUser() {
        userData.value = FirebaseAuthHelper.instance.getUser()
    }


    fun logout() {
        FirebaseAuthHelper.instance.logout()
    }
}