package com.coolightman.note.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.coolightman.note.databinding.FragmentNotesBinding
import com.google.android.material.snackbar.Snackbar

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[NotesViewModel::class.java]

        setObservers()
        setListeners()
    }

    private fun setListeners() {
        binding.btAddNotes.setOnClickListener {
            Toast.makeText(requireContext(), "Add note", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setObservers() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}