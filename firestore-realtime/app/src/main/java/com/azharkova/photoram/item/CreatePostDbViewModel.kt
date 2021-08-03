package com.azharkova.photoram.item

import android.util.Log
import androidx.lifecycle.ViewModel
import com.azharkova.photoram.BaseViewModel
import com.azharkova.photoram.ImageRepository
import com.azharkova.photoram.PostDbRepository
import com.azharkova.photoram.PostsRepository
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class CreatePostDbViewModel : BaseViewModel() {
    val isCreated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isChanged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val post: MutableStateFlow<PostItem?> = MutableStateFlow(null)
    var imageBytes: ByteArray? = null
    var currentPost: PostItem = PostItem()
    var id: String = ""

    fun loadPost() {
        if (id.isNotEmpty()) {
            PostDbRepository.instance.loadPost(id) {
                when (it) {
                    is Result.Success<PostItem> -> {
                        post.value = it.data
                        post.value?.let {
                            currentPost = it
                            currentPost.editedTime = "${Date()}"
                        }
                    }
                }
            }
        }
    }

    fun uploadImage(byteArray: ByteArray) {
        modelScope.launch {
            val result = ImageRepository.instance.uploadImage(byteArray)

            currentPost.imageLink = result.toString()
            if (id.isEmpty()) {
                createPost()
            }else {
                editPost()
            }
        }
    }

    fun publish(text: String) {
        currentPost.postText = text
        if (currentPost.date.isEmpty()) {
            currentPost.date = "${Date()}"
        }
        if (currentPost.timeStamp == null) {
            currentPost.timeStamp = Date()
        }
        if (imageBytes != null) {
            uploadImage(imageBytes!!)
        } else {
            if (id.isEmpty()) {
                createPost()
            }else {
                editPost()
            }
        }
    }


    fun createPost() {
        PostDbRepository.instance.createPost(currentPost) {
            when (it) {
                is Result.Success<*> -> isCreated.value = true
            }
        }
        isCreated.value = true
    }

    fun editPost() {
        PostDbRepository.instance.editPost(currentPost) {
            when(it) {
                is Result.Success<*> -> isChanged.value = true
                is Result.Error -> error.value = it.exception.message.toString()
            }
        }
        isChanged.value = true
    }
}