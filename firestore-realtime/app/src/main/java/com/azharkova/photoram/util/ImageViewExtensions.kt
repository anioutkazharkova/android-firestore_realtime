package com.azharkova.photoram.util

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File

fun ImageView.loadImage(image: String) {
    if (!image.isNullOrEmpty()) {
        Picasso.get()
            .load(image)
            .into(this)
    } else {
        this.setImageDrawable(null)
    }
}

fun ImageView.loadImage(file: File, invalidate: Boolean = false) {
    if (invalidate) {
        Picasso.get().invalidate(file)
    }
    Picasso.get().load(file).into(this)
}

fun ImageView.getBytes():ByteArray {
    this.isDrawingCacheEnabled = true
    this.buildDrawingCache()
    val bitmap = (drawable as BitmapDrawable).bitmap
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val data = baos.toByteArray()
    return data
}