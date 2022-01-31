package com.coolightman.note.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.coolightman.note.R
import com.coolightman.note.databinding.FragmentMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createBottomNavigation()
    }

    private fun createBottomNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }
}