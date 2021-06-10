package com.carlosbogdan.tradersapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*


class MainActivity : AppCompatActivity() {

    lateinit var bottomNav:BottomNavigationView
    lateinit var navController:NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setTheme(R.style.AppThemeIn)

        setContentView(R.layout.activity_main)

        bottomNav = bottom_navigation

        navController = findNavController(R.id.hostFragmentIn)
        setupBottomNavigation()

        //Setup
        val bundle:Bundle? = intent.extras
        val email:String? = bundle?.getString("email")
        val name:String? = bundle?.getString("name")

        //Guardado de preferencias
        val prefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("name", name)
        prefs.apply()
    }

    override fun onSupportNavigateUp(): Boolean {
        //return navController.navigateUp()
        return NavigationUI.navigateUp(navController,appBarConfiguration)
    }

    private fun setupBottomNavigation() {
        bottomNav.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
    }
}

