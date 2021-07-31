package com.azharkova.photoram.item

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azharkova.photoram.R

class CreatePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CreatePostFragment.newInstance(intent.getStringExtra("id")))
                .commitNow()
        }
    }
}