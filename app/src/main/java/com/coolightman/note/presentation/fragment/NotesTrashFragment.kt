package com.coolightman.note.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            toolbar.setOnMenuItemClickListener {
                when(it.itemId){
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

    private fun deleteAll(){
        viewModel.deleteAllPermanent()
        showSnackBar("All notes deleted permanently")
    }

    private fun restoreAll() {
        viewModel.restoreAll()
        showSnackBar("All notes restored")
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, 1000).show()
    }
}