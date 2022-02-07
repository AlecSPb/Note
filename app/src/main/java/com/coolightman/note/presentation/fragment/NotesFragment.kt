package com.coolightman.note.presentation.fragment

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.entity.SortNoteBy
import com.coolightman.note.presentation.MainActivity
import com.coolightman.note.presentation.adapter.NotesAdapter
import com.coolightman.note.presentation.viewmodel.NotesViewModel
import com.coolightman.note.di.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class NotesFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as NoteApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
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

        createAdapter()
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

        binding.rvNotesMain.adapter = notesAdapter

        val menu = binding.toolbar.menu
        menu.findItem(R.id.menu_change_layout)
            .setIcon(R.drawable.ic_baseline_view_list_24)
    }

    private fun setLinearLayout() {
        val recycler = binding.rvNotesMain
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvNotesMain.adapter = notesAdapter

        val menu = binding.toolbar.menu
        menu.findItem(R.id.menu_change_layout)
            .setIcon(R.drawable.ic_baseline_staggered_24)
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

    private fun createAdapter() {
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
            editUi(it)
        }

        viewModel.trashCount.observe(viewLifecycleOwner) {
            val trashResult: Pair<String, Int> = when {
                it == 0 -> " (${getString(R.string.empty)})" to R.drawable.ic_baseline_delete_outline_24
                it != 0 -> " ($it)" to R.drawable.ic_baseline_delete_24
                else -> throw RuntimeException("Wrong trash notes counter")
            }
            val trashTitle = getString(R.string.menu_trash_title) + trashResult.first
            binding.toolbar.menu.findItem(R.id.menu_trash).apply {
                title = trashTitle
                setIcon(trashResult.second)
            }
        }
    }

    private fun editUi(it: List<Note>) {
        if (it.isEmpty()) {
            showSplash()
            enableButtons(false)
        } else {
            hideSplash()
            enableButtons(true)
        }
    }

    private fun hideSplash() {
        binding.layoutSplashNotes.visibility = GONE
    }

    private fun showSplash() {
        binding.layoutSplashNotes.visibility = VISIBLE
    }

    private fun enableButtons(isEnabled: Boolean) {
        binding.toolbar.menu.apply {
            findItem(R.id.menu_show_date).isVisible = isEnabled
            findItem(R.id.menu_sort_note).isVisible = isEnabled
            findItem(R.id.menu_change_layout).isVisible = isEnabled
        }
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
                    changeDateShow(it)
                    true
                }
                R.id.menu_sort_note -> {
                    showSortDialog()
                    true
                }
                R.id.menu_change_layout -> {
                    changeLayout()
                    true
                }
                R.id.menu_settings -> {
                    launchToSettings()
                    true
                }
                R.id.menu_trash -> {
                    launchToNotesTrash()
                    true
                }
                else -> false
            }
        }
        swipeNoteListener()
    }

    private fun launchToSettings() {
        findNavController().navigate(
            NotesFragmentDirections.actionNavigationNotesToSettingsFragment()
        )
    }

    private fun changeDateShow(it: MenuItem) {
        it.isChecked = !it.isChecked
        viewModel.showDate(it.isChecked)
        saveShowDateChoice(it.isChecked)
    }

    private fun launchToNotesTrash() {
        findNavController().navigate(
            NotesFragmentDirections.actionNavigationNotesToNotesTrashFragment()
        )
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

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView

                val isCanceled = dX == 0f && !isCurrentlyActive
                if (isCanceled) {
                    super.onChildDraw(
                        c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                    )
                    return
                }

                val icon = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_delete_sweep_24
                )!!

                val iconWidth = icon.intrinsicWidth
                val iconHeight = icon.intrinsicHeight
                val itemHeight = itemView.bottom - itemView.top

                // Calculate position of icon
                val scale = requireContext().resources.displayMetrics.density
                val iconMargin = (ICON_MARGIN_DP * scale + 0.5f).toInt()
                val iconTop = itemView.top + (itemHeight - iconHeight) / 2
                val iconLeft = itemView.left + iconMargin
                val iconRight = iconLeft + iconWidth
                val iconBottom = iconTop + iconHeight

                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                icon.draw(c)

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX / RATIO_SHORTENING_SWIPE,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(binding.rvNotesMain)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, 2000).show()
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
        val dialog =
            SortNotesByDialogFragment(sortNumber) { answerSort -> dialogResultListener(answerSort) }
        dialog.show(childFragmentManager, "SortNotesByDialog")
    }

    private fun getPrefSortNumber() = preferences.getInt(PREF_SORT_NOTES, 0)

    private fun dialogResultListener(answerSort: SortNoteBy) {
        viewModel.setSortBy(answerSort)
        saveSortChoice(answerSort)
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
        private const val PREF_SORT_NOTES = "sortNoteByPreference"
        private const val PREF_IS_SHOW_DATE = "isShowDatePreference"
        private const val PREF_LAYOUT_TYPE = "layoutTypePreference"
        private const val ICON_MARGIN_DP = 12
        private const val RATIO_SHORTENING_SWIPE = 2
    }
}