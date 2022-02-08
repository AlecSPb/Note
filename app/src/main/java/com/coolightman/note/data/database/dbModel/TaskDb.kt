package com.coolightman.note.data.database.dbModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coolightman.note.domain.entity.NoteColor
import com.coolightman.note.domain.entity.TaskColor
import java.util.*

@Entity
data class TaskDb(
    @PrimaryKey(autoGenerate = true)
    val taskId: Long = 0,
    val description: String,
    val color: TaskColor,
    val dateEdit: Date,
    val dateRemind: Date,
    val isActive: Boolean = true,
    val isImportant: Boolean,
    val isDeleted: Boolean = false,
    val isReminding: Boolean
)
