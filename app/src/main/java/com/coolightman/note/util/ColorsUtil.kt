package com.coolightman.note.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.coolightman.note.R

fun getNoteColor(context: Context, color: Int): Int {
    return when (color) {
        0 -> ContextCompat.getColor(context, R.color.note_red)
        1 -> ContextCompat.getColor(context, R.color.note_orange)
        2 -> ContextCompat.getColor(context, R.color.note_yellow)
        3 -> ContextCompat.getColor(context, R.color.note_green)
        4 -> ContextCompat.getColor(context, R.color.note_blue)
        5 -> ContextCompat.getColor(context, R.color.note_dark_blue)
        else -> ContextCompat.getColor(context, R.color.note_gray)
    }
}

