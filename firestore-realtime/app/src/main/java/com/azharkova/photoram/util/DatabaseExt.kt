package com.azharkova.photoram.util

import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

suspend fun DatabaseReference.awaitsSingle(): DataSnapshot? =
    suspendCancellableCoroutine { continuation ->
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                val exception = when (error.toException()) {
                    is FirebaseException -> error.toException()
                    else -> Exception("The Firebase call for reference $this was cancelled")
                }
                continuation.resumeWithException(exception)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    continuation.resume(snapshot,{})
                } catch (exception: Exception) {
                    continuation.resumeWithException(exception)
                }
            }
        }
        continuation.invokeOnCancellation { this.removeEventListener(listener) }
        this.addListenerForSingleValueEvent(listener)
    }


fun DatabaseReference.observeValue(): Flow<DataSnapshot?> =
    callbackFlow {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot)
            }
        }
        addValueEventListener(listener)
        awaitClose { removeEventListener(listener) }
    }

inline fun<reified T> DatabaseReference.observeData(): Flow<T?> =
        observeValue().map {
            it?.getValue(T::class.java)
        }

inline fun<reified T> DatabaseReference.observeListData(): Flow<List<T>> =
    observeValue().map { dataset ->
        val data = mutableListOf<T>()
        dataset?.let {
            for (child in it.children) {
                child.getValue(T::class.java)?.let { item ->
                    data.add(item)
                }
            }
        }
        data
    }