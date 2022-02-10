package com.coolightman.note.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View

private const val ICON_MARGIN_DP = 12

fun Drawable.setStartIconBounds(itemView: View, context: Context) {
    val iconWidth = this.intrinsicWidth
    val iconHeight = this.intrinsicHeight
    val itemHeight = itemView.bottom - itemView.top

    // Calculate position of icon
    val scale = context.resources.displayMetrics.density
    val iconMargin = (ICON_MARGIN_DP * scale + 0.5f).toInt()
    val iconTop = itemView.top + (itemHeight - iconHeight) / 2
    val iconLeft = itemView.left + iconMargin
    val iconRight = iconLeft + iconWidth
    val iconBottom = iconTop + iconHeight

    this.setBounds(iconLeft, iconTop, iconRight, iconBottom)
}

fun Drawable.setEndIconBounds(itemView: View, context: Context) {
    val iconWidth = this.intrinsicWidth
    val iconHeight = this.intrinsicHeight
    val itemHeight = itemView.bottom - itemView.top

    // Calculate position of icon
    val scale = context.resources.displayMetrics.density
    val iconMargin = (ICON_MARGIN_DP * scale + 0.5f).toInt()
    val iconTop = itemView.top + (itemHeight - iconHeight) / 2
    val iconRight = itemView.right - iconMargin
    val iconLeft = iconRight - iconWidth
    val iconBottom = iconTop + iconHeight

    this.setBounds(iconLeft, iconTop, iconRight, iconBottom)
}