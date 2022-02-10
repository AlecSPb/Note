package com.coolightman.note.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
                    else -> false
                }
            }
        }
    }

    private fun launchToEditTask() {
        findNavController().navigate(
            TasksFragmentDirections.actionNavigationTasksToEditTaskFragment()
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
        Toast.makeText(requireContext(), "$taskId", Toast.LENGTH_SHORT).show()
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
}