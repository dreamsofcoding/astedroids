package com.udacity.asteroidradar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.utils.NetworkHelper

class MainViewModelFactory(
    private val database: AsteroidDatabase,
    private val networkHelper: NetworkHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(database, networkHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}