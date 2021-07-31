package com.azharkova.photoram.data

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

    var hasLike: Boolean = false
}