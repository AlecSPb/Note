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
import com.coolightman.note.di.ViewModelFactory
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.presentation.adapter.NotesTrashAdapter
import com.coolightman.note.presentation.viewmodel.NotesTrashViewModel
import com.coolightman.note.util.makeSnackbar
import com.coolightman.note.util.setStartIconBounds
import com.coolightman.note.util.setEndIconBounds
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
            editUi(it)
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
        binding.layoutSplashNotesTrash.visibility = View.GONE
    }

    private fun showSplash() {
        binding.layoutSplashNotesTrash.visibility = View.VISIBLE
    }

    private fun enableButtons(isEnabled: Boolean) {
        binding.toolbar.menu.apply {
            findItem(R.id.menu_delete_all).isVisible = isEnabled
            findItem(R.id.menu_restore_all).isVisible = isEnabled
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
                        showRestoreAllWarning()
                        true
                    }
                    R.id.menu_delete_all -> {
                        showDeleteAllWarning()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showRestoreAllWarning() {
        val dialog =
            WarningDialogFragment(
                getString(R.string.restore_all_note_earning_text),
                getString(R.string.bt_agree_restore_all_text)
            ) { answer -> restoreAll(answer) }
        dialog.show(childFragmentManager, "AllNotesRestoreWarningDialog")
    }

    private fun deleteAll(isConfirmed: Boolean) {
        if (isConfirmed) {
            viewModel.deleteAllPermanent()
            showSnackBar(getString(R.string.snack_permanent_deleted_all))
        }
    }

    private fun showDeleteAllWarning() {
        val dialog =
            WarningDialogFragment(
                getString(R.string.delete_all_note_earning_text),
                getString(R.string.bt_agree_delete_all_text)
            ) { answer -> deleteAll(answer) }
        dialog.show(childFragmentManager, "AllNotesDeleteWarningDialog")
    }

    private fun restoreAll(isConfirmed: Boolean) {
        if (isConfirmed) {
            viewModel.restoreAll()
            showSnackBar(getString(R.string.snack_all_restored))
        }
    }

    private fun showSnackBar(message: String) {
        makeSnackbar(binding.root, message)
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

                icon.setStartIconBounds(itemView, requireContext())
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

                icon.setEndIconBounds(itemView, requireContext())
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