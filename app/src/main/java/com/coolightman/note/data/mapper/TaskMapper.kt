package com.coolightman.note.data.mapper

import com.coolightman.note.data.database.dbModel.TaskDb
import com.coolightman.note.domain.entity.Task
import java.util.*

fun TaskDb.toEntity(): Task = Task(
    taskId = this.taskId,
    description = this.description,
    color = this.color,
    dateEdit = this.dateEdit,
    dateRemind = this.dateRemind,
    isActive = this.isActive,
    isImportant = this.isImportant,
    isDeleted = this.isDeleted,
    isReminding = this.isReminding
)

fun Task.toDb(): TaskDb = TaskDb(
    taskId = this.taskId,
    description = this.description,
    color = this.color,
    dateEdit = Date(System.currentTimeMillis()),
    dateRemind = this.dateRemind,
    isActive = this.isActive,
    isImportant = this.isImportant,
    isDeleted = this.isDeleted,
    isReminding = this.isReminding
)