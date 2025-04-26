package com.udacity.asteroidradar.view.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.viewmodels.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: FragmentMainBinding

    private lateinit var asteroidAdapter: AsteroidAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        observeTheViewModel()
        setupAdapter()

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun setupAdapter() {
        asteroidAdapter = AsteroidAdapter { selectedAsteroid ->
            viewModel.displayAsteroidDetails(selectedAsteroid)
        }
        binding.asteroidRecycler.adapter = asteroidAdapter

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
