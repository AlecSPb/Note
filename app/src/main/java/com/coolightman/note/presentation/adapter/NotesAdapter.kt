package com.coolightman.note.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.coolightman.note.databinding.NoteItemBinding
import com.coolightman.note.domain.entity.Note

class NotesAdapter(private val clickListener: (Long) -> Unit) :
    ListAdapter<Note, NoteViewHolder>(Note.DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        with(holder.binding) {
            setTitle(this, note.title)
            tvDescription.text = note.description
            cvNoteItemRoot.setCardBackgroundColor(
                ContextCompat.getColor(root.context, note.color.colorResId)
            )
            tvDate.text = note.dateEdit
            root.setOnClickListener { clickListener(note.noteId) }
        }
    }

    private fun setTitle(binding: NoteItemBinding, title: String) {
        if (title.isNotEmpty()) {
            binding.tvTitle.text = title
            binding.tvTitle.maxHeight = NOT_EMPTY_TITLE_MAX_HEIGHT
        } else {
            binding.tvTitle.text = ""
            binding.tvTitle.maxHeight = EMPTY_TITLE_MAX_HEIGHT
        }
    }

    companion object {
        private const val EMPTY_TITLE_MAX_HEIGHT = 20
        private const val NOT_EMPTY_TITLE_MAX_HEIGHT = 80
    }
}