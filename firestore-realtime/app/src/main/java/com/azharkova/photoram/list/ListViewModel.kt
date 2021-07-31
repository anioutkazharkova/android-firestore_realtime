package com.azharkova.photoram.list

import android.util.Log
import androidx.lifecycle.ViewModel
import com.azharkova.photoram.BaseViewModel
import com.azharkova.photoram.PostsRepository
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListViewModel : BaseViewModel() {
    val posts: MutableStateFlow<MutableList<PostItem>> = MutableStateFlow(mutableListOf())

    val selectedPost: MutableStateFlow<String> = MutableStateFlow("")

    fun startListenPosts() {
        PostsRepository.instance.startListenToPosts {
            posts.value = it.toMutableList()
            posts.tryEmit(posts.value)
        }
    }

    fun changeLike(index: Int) {
        val post = posts.value.get(index)
        PostsRepository.instance.changeLike(post) {
            when (it) {
                is Result.Success<*> -> Log.d("LIKE","OK")
                is Result.Error -> Log.d("LIKE",it.exception.toString())
            }
        }
    }


    fun selectPost(index: Int):String {
       return posts.value.get(index).uuid
    }

    fun stopListen() {
        PostsRepository.instance.stopListening()
    }
}