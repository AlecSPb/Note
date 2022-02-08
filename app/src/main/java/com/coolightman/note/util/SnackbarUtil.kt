package com.coolightman.note.util

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.coolightman.note.R
import com.google.android.material.snackbar.Snackbar

private const val COLOR_BACKGROUND = R.color.snackbar_background
private const val COLOR_TEXT = R.color.snackbar_text

fun makeSnackbarWithAnchor(view: View, message: String, anchorView: View) {
    Snackbar.make(view, message, 2000)
        .setAnchorView(anchorView)
        .setBackgroundTint(ContextCompat.getColor(view.context, COLOR_BACKGROUND))
        .withTextColor(ContextCompat.getColor(view.context, COLOR_TEXT))
        .show()
}

fun makeSnackbar(view: View, message: String) {
    Snackbar.make(view, message, 2000)
        .setBackgroundTint(ContextCompat.getColor(view.context, COLOR_BACKGROUND))
        .withTextColor(ContextCompat.getColor(view.context, COLOR_TEXT))
        .show()
}

fun Snackbar.withTextColor(color: Int): Snackbar {
    val tv = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    tv.setTextColor(color)
    return this
}