package com.coolightman.note.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.coolightman.note.NoteApp
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentEditNoteBinding
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.entity.NoteColor
import com.coolightman.note.presentation.viewmodel.EditNoteViewModel
import com.coolightman.note.presentation.viewmodel.ViewModelFactory
import com.coolightman.note.util.getCheckedIndex
import com.coolightman.note.util.setCheckedByIndex
import com.coolightman.note.util.toFullDateString
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class EditNoteFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as NoteApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[EditNoteViewModel::class.java]
    }

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EditNoteFragmentArgs>()
    private var noteId: Long = 0

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteId = args.noteId

        if (noteId != 0L) fetchNote()
        prepareNote()
        showKeyboard()
        setListeners()
    }

    private fun fetchNote() {
        viewModel.fetchNote(noteId)
        viewModel.note.observe(viewLifecycleOwner) {
            binding.apply {
                etNoteTitle.setText(it.title)
                etNoteDescription.setText(it.description)
                rgColors.setCheckedByIndex(it.color.ordinal)
            }
        }
    }

    private fun prepareNote() {
        setTitleColor()
        setDate()
    }

    private fun setDate() {
        lifecycleScope.launch {
            repeat(DEFAULT_REPEAT) {
                val currentDate = Date(System.currentTimeMillis()).toFullDateString()
                binding.tvDate.text = currentDate
                delay(MINUTE_MILLIS)
            }
        }
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
        if (isNoteValid()) {
            val note: Note = scanNoteDate()
            viewModel.saveNote(note)
            launchToMainNotes()
        } else {
            Snackbar.make(binding.root, getString(R.string.snackbar_empty_description), 1000).show()
        }
    }

    private fun launchToMainNotes() {
        findNavController().popBackStack()
    }

    private fun scanNoteDate() = Note(
        noteId = noteId,
        title = binding.etNoteTitle.text.toString().trim(),
        description = binding.etNoteDescription.text.toString().trim(),
        color = getNoteColor(),
        isEdited = getIsEdited()
    )

    private fun getIsEdited() = when (noteId) {
        0L -> false
        else -> true
    }

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
        val colorIndex = binding.rgColors.getCheckedIndex()
        return NoteColor.values()[colorIndex]
    }

    companion object {
        private const val MINUTE_MILLIS = 10 * 1000L
        private const val DEFAULT_REPEAT = 1000
    }

}