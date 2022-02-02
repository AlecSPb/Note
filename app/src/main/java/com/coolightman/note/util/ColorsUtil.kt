package com.coolightman.note.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.coolightman.note.R

fun getNoteColor(context: Context, color: Int): Int {
    return when (color) {
        0 -> ContextCompat.getColor(context, R.color.note_0)
        1 -> ContextCompat.getColor(context, R.color.note_1)
        2 -> ContextCompat.getColor(context, R.color.note_2)
        3 -> ContextCompat.getColor(context, R.color.note_3)
        4 -> ContextCompat.getColor(context, R.color.note_4)
        5 -> ContextCompat.getColor(context, R.color.note_5)
        else -> ContextCompat.getColor(context, R.color.note_6)
    }
}

