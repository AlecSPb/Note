package com.coolightman.note.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentSettingsBinding
import com.coolightman.note.domain.entity.NoteColor
import com.coolightman.note.domain.entity.StartDestination
import com.coolightman.note.domain.entity.TaskColor
import com.coolightman.note.presentation.MainActivity
import com.coolightman.note.presentation.MainActivity.Companion.PREF_START_DESTINATION
import com.coolightman.note.util.PrefConstants.PREF_IS_SHOW_NOTE_DATE
import com.coolightman.note.util.PrefConstants.PREF_NOTE_DEFAULT_COLOR
import com.coolightman.note.util.PrefConstants.PREF_TASK_DEFAULT_COLOR
import com.coolightman.note.util.getCheckedIndex
import com.coolightman.note.util.makeSnackbar
import com.coolightman.note.util.setCheckedByIndex

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val preferences by lazy {
        (requireActivity() as MainActivity).preferences
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareView()
        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_import -> {
                        importAppData()
                        true
                    }
                    R.id.menu_export -> {
                        exportAppData()
                        true
                    }
                    else -> false
                }
            }

            rgDefaultNoteColor.setOnCheckedChangeListener { radioGroup, i ->
                setNoteTitleColor()
                saveNoteDefaultColor()
            }

            rgDefaultTaskColor.setOnCheckedChangeListener { radioGroup, i ->
                setTaskTitleColor()
                saveTaskDefaultColor()
            }

            rgSettingsStart.setOnCheckedChangeListener { radioGroup, i ->
                saveStartDestination()
            }

            switchShowNoteDate.setOnCheckedChangeListener { compoundButton, b ->
                saveIsShowNoteDate()
            }
        }
    }

    private fun exportAppData() {
        Toast.makeText(requireActivity(), "Try to export app data", Toast.LENGTH_SHORT).show()
    }

    private fun importAppData() {
        Toast.makeText(requireActivity(), "Try to import app data", Toast.LENGTH_SHORT).show()
    }

    private fun setNoteTitleColor() {
        val noteColor = getNoteTitleColor()
        binding.tvDefaultNoteColor.setBackgroundColor(
            ContextCompat.getColor(requireContext(), noteColor.colorResId)
        )
    }

    private fun setTaskTitleColor() {
        val taskColor = getTaskTitleColor()
        binding.tvDefaultTaskColor.setBackgroundColor(
            ContextCompat.getColor(requireContext(), taskColor.colorResId)
        )
    }

    private fun getNoteTitleColor(): NoteColor {
        val colorIndex = binding.rgDefaultNoteColor.getCheckedIndex()
        return NoteColor.values()[colorIndex]
    }

    private fun getTaskTitleColor(): TaskColor {
        val colorIndex = binding.rgDefaultTaskColor.getCheckedIndex()
        return TaskColor.values()[colorIndex]
    }

    private fun saveIsShowNoteDate() {
        val isShow = binding.switchShowNoteDate.isChecked
        preferences.edit().putBoolean(PREF_IS_SHOW_NOTE_DATE, isShow).apply()
    }

    private fun saveNoteDefaultColor() {
        val checkedIndex = binding.rgDefaultNoteColor.getCheckedIndex()
        val color = NoteColor.values()[checkedIndex]
        preferences.edit().putInt(PREF_NOTE_DEFAULT_COLOR, color.ordinal).apply()
    }

    private fun saveStartDestination() {
        val checkedIndex = binding.rgSettingsStart.getCheckedIndex()
        val destination = StartDestination.values()[checkedIndex]
        preferences.edit().putInt(PREF_START_DESTINATION, destination.ordinal).apply()
    }

    private fun saveTaskDefaultColor() {
        val checkedIndex = binding.rgDefaultTaskColor.getCheckedIndex()
        val color = TaskColor.values()[checkedIndex]
        preferences.edit().putInt(PREF_TASK_DEFAULT_COLOR, color.ordinal).apply()
    }

    private fun prepareView() {
        setStartDestination()
        setNoteDefaultColor()
        setShowNoteDate()
        setTaskDefaultColor()
    }

    private fun setTaskDefaultColor() {
        val colorIndex = preferences.getInt(PREF_TASK_DEFAULT_COLOR, 4)
        val color = TaskColor.values()[colorIndex]
        binding.rgDefaultTaskColor.setCheckedByIndex(color.ordinal)
        setTaskTitleColor()
    }

    private fun setShowNoteDate() {
        val isShow = preferences.getBoolean(PREF_IS_SHOW_NOTE_DATE, false)
        binding.switchShowNoteDate.isChecked = isShow
    }

    private fun setNoteDefaultColor() {
        val colorIndex = preferences.getInt(PREF_NOTE_DEFAULT_COLOR, 5)
        val color = NoteColor.values()[colorIndex]
        binding.rgDefaultNoteColor.setCheckedByIndex(color.ordinal)
        setNoteTitleColor()
    }

    private fun setStartDestination() {
        val startNumber = preferences.getInt(PREF_START_DESTINATION, 0)
        val destination = StartDestination.values()[startNumber]
        binding.rgSettingsStart.setCheckedByIndex(destination.ordinal)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}