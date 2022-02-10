package com.coolightman.note.domain.entity

import androidx.recyclerview.widget.DiffUtil

data class Note(
    val noteId: Long = 0,
    val title: String,
    val description: String,
    val dateEdit: String = "",
    val color: NoteColor,
    val isShowingDate: Boolean = false,
    val isEdited: Boolean,
    val isDeleted: Boolean = false
) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.noteId == newItem.noteId
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    }
}
