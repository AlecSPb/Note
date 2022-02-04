package com.coolightman.note.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentDialogSortNotesBinding
import com.coolightman.note.domain.entity.SortNoteBy

class SortNotesByDialogFragment(
    private val checkedRadio: Int,
    private val resultListener: (SortNoteBy) -> Unit
) : DialogFragment() {

    private var _binding: FragmentDialogSortNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogSortNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTransparentBackground()
        setCurrentCheck()
        setListeners()
    }

    private fun setCurrentCheck() {
        val radio = when (checkedRadio) {
            0 -> binding.radioSortByColor
            1 -> binding.radioSortByColorDesc
            2 -> binding.radioSortByDate
            3 -> binding.radioSortByDateDesc
            4 -> binding.radioSortByEditDate
            5 -> binding.radioSortByEditDateDesc
            else -> throw RuntimeException("Wrong radio index in dialog")
        }
        radio.isChecked = true
    }

    private fun setTransparentBackground() {
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    private fun setListeners() {
        binding.btDialogCancel.setOnClickListener {
            dismiss()
        }

        binding.btDialogSubmit.setOnClickListener {
            val group = binding.rgSortNotes
            val selectedId = group.checkedRadioButtonId
            val radio = group.findViewById<RadioButton>(selectedId)
            val selectIndex = group.indexOfChild(radio)
            val sortNoteBy = SortNoteBy.values()[selectIndex]
            resultListener(sortNoteBy)
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}