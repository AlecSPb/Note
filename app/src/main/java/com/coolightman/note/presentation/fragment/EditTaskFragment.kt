package com.coolightman.note.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.coolightman.note.NoteApp
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentEditTaskBinding
import com.coolightman.note.di.ViewModelFactory
import com.coolightman.note.domain.entity.Task
import com.coolightman.note.domain.entity.TaskColor
import com.coolightman.note.presentation.MainActivity
import com.coolightman.note.presentation.viewmodel.EditTaskViewModel
import com.coolightman.note.util.PrefConstants
import com.coolightman.note.util.getCheckedIndex
import com.coolightman.note.util.makeSnackbarWithAnchor
import com.coolightman.note.util.setCheckedByIndex
import javax.inject.Inject

class EditTaskFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as NoteApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[EditTaskViewModel::class.java]
    }

    private val preferences by lazy {
        (requireActivity() as MainActivity).preferences
    }

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EditTaskFragmentArgs>()
    private var task: Task? = null
    private var taskId: Long = 0

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskId = args.taskId

        prepareTask()
        if (taskId != 0L) fetchTask()
        showKeyboard()
        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            btSaveBottom.setOnClickListener {
                saveTask()
            }

            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            rgColors.setOnCheckedChangeListener { _, _ ->
                setTaskColor()
            }

            switchImportantTask.setOnCheckedChangeListener { _, checked ->
                when {
                    checked -> imgImportance.visibility = VISIBLE
                    else -> imgImportance.visibility = GONE
                }
            }
        }
    }

    private fun saveTask() {
        if (isTaskValid()) {
            val task: Task = scanTaskDate()
            viewModel.saveTask(task)
            launchToMainTasks()
        }
    }

    private fun scanTaskDate(): Task {
        return Task(
            taskId = taskId,
            description = binding.etTaskDescription.text.toString().trim(),
            color = getTaskColor(),
            isImportant = binding.switchImportantTask.isChecked,
            isActive = task?.isActive ?: true
        )
    }

    private fun launchToMainTasks() {
        findNavController().popBackStack()
    }

    private fun isTaskValid(): Boolean {
        return if (binding.etTaskDescription.text.toString().trim().isNotEmpty()) {
            true
        } else {
            showSnackBar(getString(R.string.snackbar_empty_description_t))
            false
        }
    }

    private fun fetchTask() {
        viewModel.fetchTask(taskId)
        viewModel.task.observe(viewLifecycleOwner) {
            binding.apply {
                task = it
                etTaskDescription.setText(it.description)
                rgColors.setCheckedByIndex(it.color.ordinal)
                switchImportantTask.isChecked = it.isImportant
            }
        }
    }

    private fun prepareTask() {
        setDefaultTaskColor()
    }

    private fun setDefaultTaskColor() {
        val colorIndex = preferences.getInt(PrefConstants.PREF_TASK_DEFAULT_COLOR, 4)
        val taskColor = TaskColor.values()[colorIndex]
        binding.rgColors.setCheckedByIndex(taskColor.ordinal)
        setTaskColor()
    }

    private fun setTaskColor() {
        val taskColor = getTaskColor()
        binding.cvEditTask.setCardBackgroundColor(
            ContextCompat.getColor(requireContext(), taskColor.colorResId)
        )
    }

    private fun getTaskColor(): TaskColor {
        val colorIndex = binding.rgColors.getCheckedIndex()
        return TaskColor.values()[colorIndex]
    }

    private fun showKeyboard() {
        val editText = binding.etTaskDescription
        editText.requestFocus()
        val inputMethManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun showSnackBar(message: String) {
        binding.apply {
            makeSnackbarWithAnchor(root, message, btSaveBottom)
        }
    }
}