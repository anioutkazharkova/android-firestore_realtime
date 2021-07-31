package com.azharkova.photoram

import android.net.Uri
import com.azharkova.photoram.util.Result
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.*

class ImageRepository {
    companion object {
        val instance = ImageRepository()
    }

    val storage = Firebase.storage

    suspend fun uploadImage(bytes: ByteArray): Uri? {
        val reference = storage.reference.child("image-${Date()}.jpg")
        var uploadTask = reference.putBytes(bytes)
        uploadTask.await()
        return reference.downloadUrl.await()
    }
}