package com.coolightman.note.domain.entity

import com.coolightman.note.R

enum class TaskColor(val colorResId: Int) {
    RED(R.color.task_red),
    YELLOW(R.color.task_yellow),
    GREEN(R.color.task_green),
    BLUE(R.color.task_blue),
    GRAY(R.color.task_gray);
}