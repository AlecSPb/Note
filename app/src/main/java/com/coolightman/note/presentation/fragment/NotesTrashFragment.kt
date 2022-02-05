package com.coolightman.note.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coolightman.note.NoteApp
import com.coolightman.note.databinding.FragmentNotesTrashBinding
import com.coolightman.note.presentation.adapter.NotesTrashAdapter
import com.coolightman.note.presentation.viewmodel.NotesTrashViewModel
import com.coolightman.note.presentation.viewmodel.ViewModelFactory
import javax.inject.Inject

class NotesTrashFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as NoteApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[NotesTrashViewModel::class.java]
    }

    private var _binding: FragmentNotesTrashBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesTrashAdapter: NotesTrashAdapter

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesTrashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createRecycler()
        setObservers()
        setListeners()
    }

    private fun createRecycler() {
        notesTrashAdapter = NotesTrashAdapter()
        notesTrashAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.rvNotesTrash.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = notesTrashAdapter
        }
    }

    private fun setObservers() {
        viewModel.trash.observe(viewLifecycleOwner) {
            notesTrashAdapter.submitList(it)

//            if (it.isEmpty()) showSplash()
//            else hideSplash()
        }
    }

    private fun setListeners() {

    }
}