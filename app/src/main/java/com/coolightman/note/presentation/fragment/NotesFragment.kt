package com.coolightman.note.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.coolightman.note.NoteApp
import com.coolightman.note.databinding.FragmentNotesBinding
import com.coolightman.note.domain.entity.SortNoteBy
import com.coolightman.note.presentation.adapter.NotesAdapter
import com.coolightman.note.presentation.viewmodel.NotesViewModel
import com.coolightman.note.presentation.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class NotesFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as NoteApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[NotesViewModel::class.java]
    }

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesAdapter: NotesAdapter

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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

        prepareView()
        createRecycler()
        setObservers()
        setListeners()
    }

    private fun prepareView() {
        val sort = SortNoteBy.COLOR_DESC
        val isShowingDate = true
        viewModel.setSortBy(sort)
        viewModel.showDate(isShowingDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createRecycler() {
        createNoteAdapter()
        binding.rvNotesMain.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = notesAdapter
        }
    }

    private fun createNoteAdapter() {
        notesAdapter = NotesAdapter { noteId -> onItemClick(noteId) }
        notesAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private fun onItemClick(noteId: Long) {
        Snackbar.make(binding.root, "Launch to note $noteId", 1000).show()
    }

    private fun setObservers() {
        viewModel.notes.observe(viewLifecycleOwner) {
            notesAdapter.submitList(it)

            if (it.isEmpty()) showSplash()
            else hideSplash()
        }
    }

    private fun hideSplash() {
        binding.layoutSplashNotes.visibility = GONE
    }

    private fun showSplash() {
        binding.layoutSplashNotes.visibility = VISIBLE
    }

    private fun setListeners() {
        binding.btAddNotes.setOnClickListener {
            findNavController().navigate(
                NotesFragmentDirections.actionNavigationNotesToEditNoteFragment()
            )
        }
    }
}