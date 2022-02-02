package com.coolightman.note.domain.entity

import com.coolightman.note.R

enum class NoteColor(val colorResId: Int) {
    RED(R.color.note_red),
    ORANGE(R.color.note_orange),
    YELLOW(R.color.note_yellow),
    GREEN(R.color.note_green),
    BLUE(R.color.note_blue),
    D_BLUE(R.color.note_dark_blue),
    GRAY(R.color.note_gray);
}