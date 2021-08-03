package com.azharkova.photoram.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Date.format(format: String): String {
    val dateFormat: DateFormat = SimpleDateFormat(format)
    val strDate: String = dateFormat.format(this)
    return strDate
}