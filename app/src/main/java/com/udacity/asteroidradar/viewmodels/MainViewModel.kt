package com.udacity.asteroidradar.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.utils.DateUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


class MainViewModel(
    private val database: AsteroidDatabase
) : ViewModel() {

    private val repository = AsteroidRepository(database)

    val picturesOfDay = repository.cachedPictures

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid


    private val _filter = MutableLiveData(AsteroidFilter.SHOW_ALL)
    val filter: LiveData<AsteroidFilter>
        get() = _filter

    private val asteroidEntities = repository.asteroidsList

    val asteroids = _filter.switchMap { currentFilter ->
        asteroidEntities.map { entityList ->
            val domainList = entityList.asDomainModel()
            when (currentFilter) {
                AsteroidFilter.SHOW_TODAY -> {
                    domainList.filter { it.closeApproachDate == DateUtils.getToday() }
                }
                AsteroidFilter.SHOW_WEEK -> {
                    val start = DateUtils.getToday()
                    val end = DateUtils.getDateNDaysFromToday(7)
                    domainList.filter { it.closeApproachDate in start..end }
                }
                AsteroidFilter.SHOW_ALL -> {
                    domainList
                }
            }
        }
    }


    init {
        loadData()
    }

    private fun loadData() {
        showLoading()
        viewModelScope.launch {
            val asteroidsDeferred = async { repository.getAsteroids() }
            val pictureDeferred = async { repository.getPictureOfDay() }

            awaitAll(asteroidsDeferred, pictureDeferred)

            hideLoading()
        }
    }

    private fun showLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    fun updateFilter(filter: AsteroidFilter) {
        _filter.value = filter
    }
}

enum class AsteroidFilter {
    SHOW_TODAY,
    SHOW_WEEK,
    SHOW_ALL
}
