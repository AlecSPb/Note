package com.coolightman.note.presentation.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.coolightman.note.R
import com.coolightman.note.presentation.viewmodel.NotesTrashViewModel

class NotesTrashFragment : Fragment() {

    companion object {
        fun newInstance() = NotesTrashFragment()
    }

    private lateinit var viewModel: NotesTrashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes_trash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NotesTrashViewModel::class.java]
    }
}