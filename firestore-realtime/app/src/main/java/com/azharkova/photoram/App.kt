package com.azharkova.photoram

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import dagger.hilt.android.HiltAndroidApp
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase


@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()
        FirebaseFirestore.getInstance().firestoreSettings = settings
        Firebase.database.setPersistenceEnabled(true)
        appContext = this.applicationContext
    }
}