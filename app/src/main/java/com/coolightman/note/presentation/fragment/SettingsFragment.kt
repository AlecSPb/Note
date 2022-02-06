package com.coolightman.note.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.coolightman.note.databinding.FragmentSettingsBinding
import com.coolightman.note.domain.entity.StartDestination
import com.coolightman.note.presentation.MainActivity
import com.coolightman.note.presentation.MainActivity.Companion.PREF_START_DESTINATION
import com.coolightman.note.util.getCheckedIndex
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
            btSettingsSave.setOnClickListener {
                saveSettings()
                launchPreviousFragment()
            }

            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun launchPreviousFragment() {
        findNavController().popBackStack()
    }

    private fun saveSettings() {
        saveStartDestination()
    }

    private fun saveStartDestination() {
        val checkedIndex = binding.rgSettingsStart.getCheckedIndex()
        val destination = StartDestination.values()[checkedIndex]
        preferences.edit().putInt(PREF_START_DESTINATION, destination.ordinal).apply()
    }

    private fun prepareView() {
        setStartDestination()
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