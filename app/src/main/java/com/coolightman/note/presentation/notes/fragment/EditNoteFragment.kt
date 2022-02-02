package com.coolightman.note.presentation.notes.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentEditNoteBinding
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.entity.NoteColor
import com.coolightman.note.presentation.notes.viewmodel.EditNoteViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

class EditNoteFragment : Fragment() {

    private lateinit var viewModel: EditNoteViewModel

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private var noteId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[EditNoteViewModel::class.java]

        setTitleColor()
        showKeyboard()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showKeyboard() {
        val editText = binding.etNoteDescription
        editText.requestFocus()
        val inputMethManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setListeners() {
        binding.btSaveBottom.setOnClickListener {
            saveNote()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.rgColors.setOnCheckedChangeListener { group, checkedId ->
            setTitleColor()
        }
    }

    private fun saveNote() {
        if (isNoteValid()){
            val note: Note = scanNoteDate()
            viewModel.saveNote(note)
            Snackbar.make(binding.root, getString(R.string.snackbar_note_saved), LENGTH_SHORT).show()
        }else{
            Snackbar.make(binding.root, getString(R.string.snackbar_empty_description), LENGTH_SHORT).show()
        }
    }

    private fun scanNoteDate() = Note(
        noteId = noteId,
        title = binding.etNoteTitle.text.toString().trim(),
        description = binding.etNoteDescription.text.toString().trim(),
        color = getNoteColor()
    )

    private fun isNoteValid(): Boolean {
        return binding.etNoteDescription.text.toString().trim().isNotEmpty()
    }

    private fun setTitleColor() {
        val noteColor = getNoteColor()
        binding.cvEditNote.setCardBackgroundColor(
            ContextCompat.getColor(requireContext(), noteColor.colorResId)
        )
    }

    private fun getNoteColor(): NoteColor {
        val group = binding.rgColors
        val checkedId = group.checkedRadioButtonId
        val radioButton = group.findViewById<RadioButton>(checkedId)
        val colorIndex = group.indexOfChild(radioButton)
        return NoteColor.values()[colorIndex]
    }

}