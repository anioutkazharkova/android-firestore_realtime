package com.azharkova.photoram.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.azharkova.photoram.BaseViewModel
import com.azharkova.photoram.PostsRepository
import com.azharkova.photoram.data.CommentItem
import com.azharkova.photoram.data.PostItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class PostItemViewModel : BaseViewModel() {
    val isDeleted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var id: String = ""
    val post: MutableStateFlow<PostItem?> = MutableStateFlow(null)
    val comments: MutableStateFlow<MutableList<CommentItem>> = MutableStateFlow(mutableListOf())

    val errorb = MutableStateFlow("")

    fun loadPost() {
        modelScope.launch {
            post.value = PostsRepository.instance.loadPost(id)
            listenComments()
        }
    }

    fun listenComments() {
         PostsRepository.instance.startListenToComments(id) {
               comments.value = it.toMutableList()
           }
    }

    fun deletePost() {
        modelScope.launch {
            try {
                PostsRepository.instance.deletePost(id).also {
                    isDeleted.value = true
                }
            }
            catch (e: Exception){
                Log.d("ERROR",e.message.toString())
                errorb.tryEmit(e.message.toString())
            }
        }
    }

    fun stopListenComments() {
        PostsRepository.instance.stopCommentListening()
    }

    fun sendComment(text: String) {
        val commentItem = CommentItem()
        commentItem.date = Date()
        commentItem.postId = id
        commentItem.text = text
        modelScope.launch {
            PostsRepository.instance.sendComment(commentItem)
        }
    }

}