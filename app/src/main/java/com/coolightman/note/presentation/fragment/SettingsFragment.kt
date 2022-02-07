package com.coolightman.note.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentSettingsBinding
import com.coolightman.note.domain.entity.NoteColor
import com.coolightman.note.domain.entity.StartDestination
import com.coolightman.note.presentation.MainActivity
import com.coolightman.note.presentation.MainActivity.Companion.PREF_START_DESTINATION
import com.coolightman.note.util.getCheckedIndex
import com.coolightman.note.util.setCheckedByIndex
import com.google.android.material.snackbar.Snackbar

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
            btSettingsSave.setOnClickListener {
                saveSettings()
                showSnackBar(getString(R.string.settings_saved_text))
            }

            toolbar.setNavigationOnClickListener {
                launchPreviousFragment()
            }

            rgDefaultNoteColor.setOnCheckedChangeListener { radioGroup, i ->
                setTitleColor()
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, 2000)
            .setAnchorView(binding.btSettingsSave)
            .show()
    }

    private fun setTitleColor() {
        val color = getTitleColor()
        binding.tvDefaultNoteColor.setBackgroundColor(
            ContextCompat.getColor(requireContext(), color.colorResId)
        )
    }

    private fun getTitleColor(): NoteColor {
        val colorIndex = binding.rgDefaultNoteColor.getCheckedIndex()
        return NoteColor.values()[colorIndex]
    }

    private fun launchPreviousFragment() {
        findNavController().popBackStack()
    }

    private fun saveSettings() {
        saveStartDestination()
        saveNoteDefaultColor()
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

    private fun prepareView() {
        setStartDestination()
        setNoteDefaultColor()
    }

    private fun setNoteDefaultColor() {
        val colorIndex = preferences.getInt(PREF_NOTE_DEFAULT_COLOR, 6)
        val color = NoteColor.values()[colorIndex]
        binding.rgDefaultNoteColor.setCheckedByIndex(color.ordinal)
        setTitleColor()
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

    companion object {
        const val PREF_NOTE_DEFAULT_COLOR = "noteDefaultColorPreference"
    }
}