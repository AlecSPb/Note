package com.coolightman.note.presentation.fragment

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coolightman.note.NoteApp
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentNotesTrashBinding
import com.coolightman.note.presentation.adapter.NotesTrashAdapter
import com.coolightman.note.presentation.viewmodel.NotesTrashViewModel
import com.coolightman.note.presentation.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

            if (it.isEmpty()) showSplash()
            else hideSplash()
        }
    }

    private fun hideSplash() {
        binding.layoutSplashNotesTrash.visibility = View.GONE
        enableButtons(true)
    }

    private fun showSplash() {
        binding.layoutSplashNotesTrash.visibility = View.VISIBLE
        enableButtons(false)
    }

    private fun enableButtons(isEnabled: Boolean) {
        binding.toolbar.menu.apply {
            findItem(R.id.menu_delete_all).isEnabled = isEnabled
            findItem(R.id.menu_restore_all).isEnabled = isEnabled
        }
    }

    private fun setListeners() {
        swipeNoteRightListener()
        swipeNoteLeftListener()

        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_restore_all -> {
                        restoreAll()
                    }
                    R.id.menu_delete_all -> {
                        deleteAll()
                    }
                }
                true
            }
        }
    }

    private fun deleteAll() {
        viewModel.deleteAllPermanent()
        showSnackBar(getString(R.string.snack_permanent_deleted_all))
    }

    private fun restoreAll() {
        viewModel.restoreAll()
        showSnackBar(getString(R.string.snack_all_restored))
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, 1000).show()
    }

    private fun swipeNoteRightListener() {
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
                val note = notesTrashAdapter.currentList[position]
                viewModel.deletePermanent(note.noteId)
                showSnackBar(getString(R.string.snack_permanent_deleted_note))
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
                    R.drawable.ic_baseline_delete_forever_36
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
        ItemTouchHelper(callback).attachToRecyclerView(binding.rvNotesTrash)
    }

    private fun swipeNoteLeftListener() {
        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val note = notesTrashAdapter.currentList[position]
                viewModel.restoreFromTrash(note.noteId)
                showSnackBar(getString(R.string.snack_note_restored))
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
                    R.drawable.ic_baseline_restore_from_trash_36
                )!!

                val iconWidth = icon.intrinsicWidth
                val iconHeight = icon.intrinsicHeight
                val itemHeight = itemView.bottom - itemView.top

                // Calculate position of icon
                val scale = requireContext().resources.displayMetrics.density
                val iconMargin = (ICON_MARGIN_DP * scale + 0.5f).toInt()
                val iconTop = itemView.top + (itemHeight - iconHeight) / 2
                val iconRight = itemView.right - iconMargin
                val iconLeft = iconRight - iconWidth
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
        ItemTouchHelper(callback).attachToRecyclerView(binding.rvNotesTrash)
    }

    companion object {
        private const val ICON_MARGIN_DP = 12
        private const val RATIO_SHORTENING_SWIPE = 2
    }
}