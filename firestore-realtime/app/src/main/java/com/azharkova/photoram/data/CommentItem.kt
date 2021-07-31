package com.azharkova.photoram.data

import java.util.*

class CommentItem {
        var uuid = UUID.randomUUID().toString()
        var text: String = ""
        var date: Date? = null
        var userId: String = ""
        var userName: String = ""
        var postId: String = ""
}