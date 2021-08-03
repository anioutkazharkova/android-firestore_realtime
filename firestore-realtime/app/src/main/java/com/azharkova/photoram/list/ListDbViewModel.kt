package com.azharkova.photoram.list

import android.util.Log
import androidx.lifecycle.ViewModel
import com.azharkova.photoram.BaseViewModel
import com.azharkova.photoram.PostDbRepository
import com.azharkova.photoram.PostsRepository
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListDbViewModel : BaseViewModel() {
    val posts: MutableStateFlow<MutableList<PostItem>> = MutableStateFlow(mutableListOf())

    val selectedPost: MutableStateFlow<String> = MutableStateFlow("")


    fun startListenPosts() {
        PostDbRepository.instance.startListenToPosts {
            when (it) {
                is Result.Success<List<PostItem>> ->  posts.value = it.data.toMutableList()
            }
        }
    }

    fun changeLike(index: Int) {
        val post = posts.value.get(index)
        PostDbRepository.instance.changeLike(post) {
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
        PostDbRepository.instance.stopListen()
    }
}