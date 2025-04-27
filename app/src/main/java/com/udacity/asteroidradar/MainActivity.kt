package com.udacity.asteroidradar

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.utils.NetworkHelper
import com.udacity.asteroidradar.view.detail.DetailFragmentArgs
import com.udacity.asteroidradar.viewmodels.MainViewModel
import com.udacity.asteroidradar.viewmodels.MainViewModelFactory
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            AsteroidDatabase.getDatabase(this),
            NetworkHelper(this)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host_fragment)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, appBarConfiguration)

        setupListeners()
    }

    private fun setupListeners() {
        mainViewModel.asteroids.observe(this) {
            navController.currentDestination?.let { destination -> updateAppBarTitle(destination) }
        }

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            updateAppBarTitle(destination, arguments)
        }
    }

    private fun updateAppBarTitle(destination: NavDestination, arguments: Bundle? = null) {
        val appName = getString(R.string.app_name)

        when (destination.id) {
            R.id.mainFragment -> {
                val asteroids = mainViewModel.asteroids.value.orEmpty()

                val hasHazardous = asteroids.any { it.isPotentiallyHazardous }

                val statusMessage = if (hasHazardous) "Hazard!" else "Safe"

                supportActionBar?.title =
                    if (asteroids.isNotEmpty()) "$appName - $statusMessage" else appName
            }

            R.id.detailFragment -> {
                val asteroidName = arguments?.let {
                    DetailFragmentArgs.fromBundle(it).selectedAsteroid.codename
                } ?: "Asteroid Details"
                supportActionBar?.title = "$appName - $asteroidName"
            }

            else -> {
                supportActionBar?.title = appName
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()

    }
}
