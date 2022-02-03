package com.coolightman.note.util

import java.text.SimpleDateFormat
import java.util.*

fun Date?.toDateString(): String {
    val defPattern = "dd-MM-yyyy"
    val format = SimpleDateFormat(defPattern, Locale.getDefault())
    return this?.let { format.format(this) }.orEmpty()
}

fun Date?.toFullDateString(): String {
    val defPattern = "dd-MM-yyyy HH:mm"
    val format = SimpleDateFormat(defPattern, Locale.getDefault())
    return this?.let { format.format(this) }.orEmpty()
}