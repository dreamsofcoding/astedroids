package com.udacity.asteroidradar.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.viewmodels.AsteroidFilter
import com.udacity.asteroidradar.viewmodels.MainViewModel
import com.udacity.asteroidradar.viewmodels.MainViewModelFactory

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(AsteroidDatabase.getDatabase(requireActivity()))
    }

    private lateinit var binding: FragmentMainBinding

    private lateinit var asteroidAdapter: AsteroidAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeTheViewModel()
        setupAsteroidAdapter()
        setupPhotoOfTheDayAdapter()

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_overflow_menu, menu)

                viewModel.filter.observe(viewLifecycleOwner) { currentFilter ->

                    menu.findItem(R.id.show_today_menu)?.isChecked = false
                    menu.findItem(R.id.show_week_menu)?.isChecked = false
                    menu.findItem(R.id.show_all_menu)?.isChecked = false

                    when (currentFilter) {
                        AsteroidFilter.SHOW_TODAY -> menu.findItem(R.id.show_today_menu)?.isChecked =
                            true

                        AsteroidFilter.SHOW_WEEK -> menu.findItem(R.id.show_week_menu)?.isChecked =
                            true

                        AsteroidFilter.SHOW_ALL -> menu.findItem(R.id.show_all_menu)?.isChecked =
                            true
                    }
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.show_today_menu -> {
                        viewModel.updateFilter(AsteroidFilter.SHOW_TODAY)
                        true
                    }

                    R.id.show_week_menu -> {
                        viewModel.updateFilter(AsteroidFilter.SHOW_WEEK)
                        true
                    }

                    R.id.show_all_menu -> {
                        viewModel.updateFilter(AsteroidFilter.SHOW_ALL)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupPhotoOfTheDayAdapter() {
        val adapter = PictureAdapter()
        binding.pictureOfDayPager.apply {
            this.adapter = adapter
        }

        viewModel.picturesOfDay.observe(viewLifecycleOwner) { pictures ->
            adapter.submitList(pictures.reversed())
            binding.pictureOfDayPager.setCurrentItem(adapter.itemCount - 1, false)
        }
    }

    private fun setupAsteroidAdapter() {
        asteroidAdapter = AsteroidAdapter { selectedAsteroid ->
            viewModel.displayAsteroidDetails(selectedAsteroid)
        }
        binding.asteroidRecycler.apply {
            adapter = asteroidAdapter
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        }
    }

    private fun observeTheViewModel() {
        viewModel.asteroids.observe(viewLifecycleOwner) { asteroids ->
            asteroidAdapter.submitList(asteroids)
        }

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) { asteroid ->
            asteroid?.let {
                val action = MainFragmentDirections.actionShowDetail(asteroid)
                findNavController().navigate(action)
                viewModel.displayAsteroidDetailsComplete()
            }
        }
    }
}
