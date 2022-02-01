package com.coolightman.note.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.coolightman.note.R
import com.coolightman.note.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        createBottomNavigation()
        hideBottomNavigation()
    }

    private fun createBottomNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavBar.setupWithNavController(navController)
    }

    private fun hideBottomNavigation() {
        val navBar = binding.bottomNavBar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.editNoteFragment) {
                navBar.visibility = View.GONE
                supportActionBar?.hide()
            } else {
                navBar.visibility = View.VISIBLE
                supportActionBar?.show()
            }
        }
    }


}