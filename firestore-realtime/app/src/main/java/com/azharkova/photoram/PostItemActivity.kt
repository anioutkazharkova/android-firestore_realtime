package com.azharkova.photoram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azharkova.photoram.ui.main.PostItemFragment

class PostItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_item_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PostItemFragment.newInstance(intent.getStringExtra("id").orEmpty()))
                .commitNow()
        }
    }
}