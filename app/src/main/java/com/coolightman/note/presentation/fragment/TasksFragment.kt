package com.coolightman.note.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.coolightman.note.NoteApp
import com.coolightman.note.databinding.FragmentTasksBinding
import com.coolightman.note.presentation.viewmodel.TasksViewModel
import com.coolightman.note.di.ViewModelFactory
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

        setObservers()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
        binding.btAddTasks.setOnClickListener {
            Toast.makeText(requireContext(), "Add task", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setObservers() {

    }

    private fun showSnackBar(message: String) {
        binding.apply {
            makeSnackbarWithAnchor(root, message, btAddTasks)
        }
    }
}