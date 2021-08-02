package com.azharkova.photoram.data

import com.google.firebase.database.Exclude
import java.util.*
import kotlin.collections.ArrayList

class PostItem{
    var uuid: String = UUID.randomUUID().toString()
    var imageLink: String = ""
    var postText: String = ""
    var date:String = ""
    var userId: String = ""
    var userName: String = ""
    var likeItems: ArrayList<LikeItem> = arrayListOf()
    var editedTime: String? = null
    var editor: String? = null

    var timeStamp: Date? = null

    @Exclude
    @com.google.firebase.firestore.Exclude
    var hasLike: Boolean = false
}