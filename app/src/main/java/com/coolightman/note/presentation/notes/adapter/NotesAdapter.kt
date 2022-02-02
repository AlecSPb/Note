package com.coolightman.note.presentation.notes.adapter

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
            tvTitle.text = note.title
            tvDescription.text = note.description
            cvNoteItemRoot.setCardBackgroundColor(
                ContextCompat.getColor(root.context, note.color.colorResId)
            )
            root.setOnClickListener { clickListener(note.noteId) }
        }
    }
}