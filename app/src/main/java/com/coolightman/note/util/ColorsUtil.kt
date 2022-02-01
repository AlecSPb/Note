package com.coolightman.note.util

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.coolightman.note.R


object ColorsUtil {

    fun getColor(context: Context, color: Int): Int {
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

    fun setActionBarColor(appCompatActivity: AppCompatActivity, colorId: Int) {
        val actionBar: ActionBar? = appCompatActivity.supportActionBar
        val colorDrawable = ColorDrawable(getColorBar(appCompatActivity, colorId))
        actionBar?.setBackgroundDrawable(colorDrawable)
    }

    @Suppress("DEPRECATION")
    private fun getColorBar(context: Context, id: Int): Int {
        val version = Build.VERSION.SDK_INT
        return if (version >= 23) {
            ContextCompat.getColor(context, id)
        } else {
            context.resources.getColor(id)
        }
    }

}