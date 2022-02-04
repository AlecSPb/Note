package com.coolightman.note.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.coolightman.note.NoteApp
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentNotesBinding
import com.coolightman.note.domain.entity.LayoutType
import com.coolightman.note.domain.entity.SortNoteBy
import com.coolightman.note.presentation.MainActivity
import com.coolightman.note.presentation.adapter.NotesAdapter
import com.coolightman.note.presentation.viewmodel.NotesViewModel
import com.coolightman.note.presentation.viewmodel.ViewModelFactory
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

    private val preferences by lazy {
        (requireActivity() as MainActivity).preferences
    }

    private lateinit var layoutType: LayoutType

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

        createRecycler()
        prepareView()
        setObservers()
        setListeners()
    }

    private fun prepareView() {
        setSortNotes()
        setIsShowingDate()
        setLayout()
    }

    private fun setLayout() {
        getPrefLayout()
        when (layoutType) {
            LayoutType.LINE -> setLinearLayout()
            LayoutType.GRID -> setGridLayout()
        }
    }

    private fun setGridLayout() {
        val recycler = binding.rvNotesMain
        recycler.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val menu = binding.toolbar.menu
        menu.findItem(R.id.menu_change_layout)
            .setIcon(R.drawable.ic_baseline_view_linear_24)
    }

    private fun setLinearLayout() {
        val recycler = binding.rvNotesMain
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val menu = binding.toolbar.menu
        menu.findItem(R.id.menu_change_layout)
            .setIcon(R.drawable.ic_baseline_grid_24)
    }

    private fun getPrefLayout() {
        val layoutTypeNumber = preferences.getInt(PREF_LAYOUT_TYPE, 0)
        layoutType = LayoutType.values()[layoutTypeNumber]
    }

    private fun setIsShowingDate() {
        val isShowingDate = getPrefIsShowDate()
        viewModel.showDate(isShowingDate)
        binding.toolbar.menu.findItem(R.id.menu_show_date).isChecked = isShowingDate
    }

    private fun setSortNotes() {
        val sortNumber = getPrefSortNumber()
        viewModel.setSortBy(SortNoteBy.values()[sortNumber])
    }

    private fun getPrefIsShowDate() = preferences.getBoolean(PREF_IS_SHOW_DATE, false)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createRecycler() {
        notesAdapter = NotesAdapter { noteId -> onItemClick(noteId) }
        binding.rvNotesMain.adapter = notesAdapter
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
                R.id.menu_change_layout -> {
                    changeLayout()
                }
            }
            true
        }
        swipeNoteListener()
    }

    private fun swipeNoteListener() {
        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val note = notesAdapter.currentList[position]
                viewModel.sendToTrashBasket(note.noteId)
                showSnackBar(getString(R.string.sent_to_trash))
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(binding.rvNotesMain)
    }

    private fun showSnackBar(message: String){
        Snackbar.make(binding.root, message, 1000).show()
    }

    private fun changeLayout() {
        when (layoutType) {
            LayoutType.LINE -> {
                layoutType = LayoutType.GRID
                saveLayoutChoice()
                setLayout()
            }
            LayoutType.GRID -> {
                layoutType = LayoutType.LINE
                saveLayoutChoice()
                setLayout()
            }
        }
    }

    private fun showSortDialog() {
        val sortNumber = getPrefSortNumber()
        val dialog = SortNotesByDialogFragment(sortNumber) { dialogResultListener(it) }
        dialog.show(childFragmentManager, "SortNotesByDialog")
    }

    private fun getPrefSortNumber() = preferences.getInt(PREF_SORT_NOTES, 0)

    private fun dialogResultListener(sortNoteBy: SortNoteBy) {
        viewModel.setSortBy(sortNoteBy)
        saveSortChoice(sortNoteBy)
    }

    private fun saveSortChoice(sortNoteBy: SortNoteBy) {
        preferences.edit().putInt(PREF_SORT_NOTES, sortNoteBy.ordinal).apply()
    }

    private fun saveShowDateChoice(checked: Boolean) {
        preferences.edit().putBoolean(PREF_IS_SHOW_DATE, checked).apply()
    }

    private fun saveLayoutChoice() {
        preferences.edit().putInt(PREF_LAYOUT_TYPE, layoutType.ordinal).apply()
    }

    companion object {
        private const val PREF_SORT_NOTES = "SortNoteByPreference"
        private const val PREF_IS_SHOW_DATE = "isShowDatePreference"
        private const val PREF_LAYOUT_TYPE = "layoutTypePreference"
    }
}