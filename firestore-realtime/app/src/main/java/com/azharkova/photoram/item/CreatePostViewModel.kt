package com.azharkova.photoram.item

import android.util.Log
import androidx.lifecycle.ViewModel
import com.azharkova.photoram.BaseViewModel
import com.azharkova.photoram.ImageRepository
import com.azharkova.photoram.PostsRepository
import com.azharkova.photoram.data.PostItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class CreatePostViewModel : BaseViewModel() {
    val isCreated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isChanged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val post: MutableStateFlow<PostItem?> = MutableStateFlow(null)
    var imageBytes: ByteArray? = null
    var currentPost: PostItem = PostItem()
    var id: String = ""

    fun loadPost() {
        if (id.isNotEmpty()) {
            modelScope.launch {
                post.value = PostsRepository.instance.loadPost(id)
                post.value?.let {
                    currentPost = it
                    currentPost.editedTime = "${Date()}"
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
          modelScope.launch {
              try {

                  PostsRepository.instance.createPost(postItem = currentPost).also {
                      isCreated.value = true
                  }
              }
              catch (e: Exception) {
                  Log.d("ERROR",e.message.toString())
                  error.value = e.message.toString()
              }
          }
    }

    fun editPost() {
        modelScope.launch {
            try {
                PostsRepository.instance.editPost(postItem = currentPost).also {
                    isChanged.value = true
                }
            }
            catch (e: Exception) {
                Log.d("ERROR",e.message.toString())
                error.value = e.message.toString()
            }
        }
    }
}