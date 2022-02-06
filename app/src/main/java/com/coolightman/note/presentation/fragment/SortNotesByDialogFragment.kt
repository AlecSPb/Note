package com.coolightman.note.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentDialogSortNotesBinding
import com.coolightman.note.domain.entity.SortNoteBy
import com.coolightman.note.util.getCheckedIndex
import com.coolightman.note.util.setCheckedByIndex

class SortNotesByDialogFragment(
    private val sortNumber: Int,
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
        binding.rgSortNotes.setCheckedByIndex(sortNumber)
    }

    private fun setTransparentBackground() {
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    private fun setListeners() {
        binding.btDialogCancel.setOnClickListener {
            dismiss()
        }

        binding.btDialogSubmit.setOnClickListener {
            val selectIndex = binding.rgSortNotes.getCheckedIndex()
            val sortNoteBy = SortNoteBy.values()[selectIndex]
            resultListener(sortNoteBy)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}