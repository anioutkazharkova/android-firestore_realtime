package com.azharkova.photoram

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel : ViewModel() {
    val error: MutableStateFlow<String> = MutableStateFlow("")
    val uiDispatcher = Dispatchers.Main.immediate
    val ioDispatcher = Dispatchers.IO

    val job = SupervisorJob()

    val modelScope = CoroutineScope(uiDispatcher + job)
}