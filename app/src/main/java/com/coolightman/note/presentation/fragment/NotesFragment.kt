package com.coolightman.note.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coolightman.note.NoteApp
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentNotesBinding
import com.coolightman.note.domain.entity.SortNoteBy
import com.coolightman.note.presentation.adapter.NotesAdapter
import com.coolightman.note.presentation.viewmodel.NotesViewModel
import com.coolightman.note.presentation.viewmodel.ViewModelFactory
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
        val isShowingDate = false
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
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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
        findNavController().navigate(
            NotesFragmentDirections.actionNavigationNotesToEditNoteFragment(noteId)
        )
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

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_show_date -> {
                    it.isChecked = !it.isChecked
                    viewModel.showDate(it.isChecked)
                    saveShowDateChoice(it.isChecked)
                }
                R.id.menu_sort_note -> {
                    showSortDialog()
                }
            }
            true
        }
    }

    private fun showSortDialog() {
        val dialog = SortNotesByDialogFragment { dialogResultListener(it) }
        dialog.show(childFragmentManager, "SortNotesByDialog")
    }

    private fun dialogResultListener(sortNoteBy: SortNoteBy) {
        Toast.makeText(requireContext(), "$sortNoteBy", Toast.LENGTH_SHORT).show()
    }

    private fun saveShowDateChoice(checked: Boolean) {

    }
}