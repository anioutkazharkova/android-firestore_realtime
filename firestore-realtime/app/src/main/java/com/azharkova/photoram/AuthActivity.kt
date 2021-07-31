package com.azharkova.photoram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azharkova.photoram.auth.AuthFragment
import com.google.firebase.FirebaseApp

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
        FirebaseApp.initializeApp(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AuthFragment.newInstance())
                .commitNow()
        }
    }
}