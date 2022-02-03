package com.coolightman.note.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.coolightman.note.databinding.FragmentDialogSortNotesBinding
import com.coolightman.note.domain.entity.SortNoteBy

class SortNotesByDialogFragment(val resultListener: (SortNoteBy) -> Unit) : DialogFragment() {

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

        setListeners()
    }

    private fun setListeners() {
        binding.btDialogCancel.setOnClickListener {
            dismiss()
        }

        binding.btDialogSubmit.setOnClickListener {
            val group = binding.rgSortNotes
            try {
                val selectedId = group.checkedRadioButtonId
                val radio = group.findViewById<RadioButton>(selectedId)
                val selectIndex = group.indexOfChild(radio)
                val sortNoteBy = SortNoteBy.values()[selectIndex]
                resultListener(sortNoteBy)
                dismiss()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Sort type not selected", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val scale = requireContext().resources.displayMetrics.density
        val width = (300 * scale + 0.5f).toInt()
        dialog?.window?.setLayout(
            width,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}