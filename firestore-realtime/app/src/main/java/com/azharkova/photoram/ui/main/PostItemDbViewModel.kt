package com.azharkova.photoram.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.azharkova.photoram.BaseViewModel
import com.azharkova.photoram.PostDbRepository
import com.azharkova.photoram.PostsRepository
import com.azharkova.photoram.data.CommentItem
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class PostItemDbViewModel : BaseViewModel() {
    val isDeleted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var id: String = ""
    val post: MutableStateFlow<PostItem?> = MutableStateFlow(null)
    val comments: MutableStateFlow<MutableList<CommentItem>> = MutableStateFlow(mutableListOf())

    val errorb = MutableStateFlow("")

    fun loadPost() {
        if (id.isNotEmpty()) {
            PostDbRepository.instance.loadPost(id) {
                when (it) {
                    is Result.Success<PostItem> -> {
                        post.value = it.data
                        listenComments()
                    }
                }
            }
        }
    }

    fun listenComments() {
        PostDbRepository.instance.startListenToComments(id) {
            when (it) {
                is Result.Success<List<CommentItem>> -> comments.value = it.data.toMutableList()
            }
        }
    }

    fun deletePost() {
        PostDbRepository.instance.deletePost(id) {
            when (it) {
                is Result<Boolean> -> isDeleted.value = true
                is Result.Error -> errorb.tryEmit(it.exception.message.toString())
            }
        }
    }

    fun stopListenComments() {
        PostDbRepository.instance.startListenToComments(id) {}
    }

    fun sendComment(text: String) {
        val commentItem = CommentItem()
        commentItem.date = Date()
        commentItem.postId = id
        commentItem.text = text
        PostDbRepository.instance.sendComment(commentItem) {
            when (it) {
                is Result.Success<*> -> {
                }
            }
        }
    }
}

