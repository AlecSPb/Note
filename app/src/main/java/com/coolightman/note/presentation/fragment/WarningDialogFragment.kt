package com.coolightman.note.presentation.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentDialogWarningBinding

class WarningDialogFragment(
    private val warningText: String,
    private val result: (Boolean) -> Unit
) : DialogFragment() {

    private var _binding: FragmentDialogWarningBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogWarningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTransparentBackground()
        setText()
        setListeners()
    }

    private fun setText() {
        binding.tvWarningText.text = warningText
    }

    private fun setTransparentBackground() {
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        result(false)
    }

    private fun setListeners() {
        binding.btDialogCancel.setOnClickListener {
            result(false)
            dismiss()
        }

        binding.btDialogYes.setOnClickListener {
            result(true)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}