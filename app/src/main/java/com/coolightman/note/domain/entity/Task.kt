package com.coolightman.note.domain.entity

import androidx.recyclerview.widget.DiffUtil
import java.util.*

data class Task(
    val taskId: Long = 0,
    val description: String,
    val color: TaskColor,
    val dateEdit: String = "",
    val dateRemind: Long,
    val isActive: Boolean = true,
    val isImportant: Boolean,
    val isDeleted: Boolean = false,
    val isReminding: Boolean
) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.taskId == newItem.taskId
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }
}
