package com.coolightman.note.presentation.fragment

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coolightman.note.NoteApp
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentTasksBinding
import com.coolightman.note.presentation.viewmodel.TasksViewModel
import com.coolightman.note.di.ViewModelFactory
import com.coolightman.note.domain.entity.Task
import com.coolightman.note.presentation.adapter.TasksAdapter
import com.coolightman.note.util.makeSnackbarWithAnchor
import com.coolightman.note.util.setStartIconBounds
import javax.inject.Inject

class TasksFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as NoteApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TasksViewModel::class.java]
    }

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var tasksAdapter: TasksAdapter

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
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

    private fun setListeners() {
        swipeTaskListener()
        binding.apply {
            btAddTasks.setOnClickListener {
                launchToEditTask()
            }

            binding.toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_settings -> {
                        launchToSettings()
                        true
                    }
                    R.id.menu_delete_inactive ->{
                        showDeleteInactiveWarning()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showDeleteInactiveWarning() {
        val dialog =
            WarningDialogFragment(
                getString(R.string.delete_inactive_tasks_earning_text),
                getString(R.string.bt_delete_text)
            ) { answer -> deleteAll(answer) }
        dialog.show(childFragmentManager, "AllNotesDeleteWarningDialog")
    }

    private fun deleteAll(isConfirmed: Boolean) {
        if (isConfirmed) {
            viewModel.deleteAllInactive()
            showSnackBar(getString(R.string.snack_inactive_task_deleted))
        }
    }

    private fun launchToEditTask() {
        findNavController().navigate(
            TasksFragmentDirections.actionNavigationTasksToEditTaskFragment()
        )
    }

    private fun launchToEditTask(taskId: Long) {
        findNavController().navigate(
            TasksFragmentDirections.actionNavigationTasksToEditTaskFragment(taskId)
        )
    }

    private fun setObservers() {
        viewModel.tasks.observe(viewLifecycleOwner){
            tasksAdapter.submitList(it)

            editUi(it)
        }
    }

    private fun editUi(list: List<Task>) {
        if (list.isEmpty()) {
            showSplash()
        } else {
            hideSplash()
        }
    }

    private fun createRecycler() {
        val recycler = binding.rvTasksMain

        tasksAdapter = TasksAdapter (
            { taskId -> onItemClick(taskId) },
            { taskId -> onCheckClick(taskId) }
        )
        tasksAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        recycler.apply {
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun onCheckClick(taskId: Long) {
        viewModel.switchActive(taskId)
    }

    private fun onItemClick(taskId: Long) {
        launchToEditTask(taskId)
    }

    private fun showSnackBar(message: String) {
        binding.apply {
            makeSnackbarWithAnchor(root, message, btAddTasks)
        }
    }

    private fun hideSplash() {
        binding.layoutSplashTasks.visibility = View.GONE
    }

    private fun showSplash() {
        binding.layoutSplashTasks.visibility = View.VISIBLE
    }

    private fun launchToSettings() {
        findNavController().navigate(
            TasksFragmentDirections.actionNavigationTasksToSettingsFragment()
        )
    }

    private fun swipeTaskListener() {
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
                val task = tasksAdapter.currentList[position]
                viewModel.deleteTask(task.taskId)
                showSnackBar(getString(R.string.task_deleted))
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
        ItemTouchHelper(callback).attachToRecyclerView(binding.rvTasksMain)
    }

    companion object {
        private const val RATIO_SHORTENING_SWIPE = 2
    }
}