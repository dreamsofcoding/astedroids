package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.PictureOfDayEntity
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.utils.Constants.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.utils.DateUtils
import com.udacity.asteroidradar.utils.NetworkHelper
import com.udacity.asteroidradar.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidRepository(
    private val database: AsteroidDatabase,
    private val networkHelper: NetworkHelper
) {

    val asteroidsList = database.asteroidDao().getAsteroids()

    suspend fun getAsteroids(): List<Asteroid> = withContext(Dispatchers.IO) {

        val today = DateUtils.getToday()

        val localAsteroids = database.asteroidDao().getAsteroidsSync(today)

        if (localAsteroids.isNotEmpty()) {
            return@withContext localAsteroids.asDomainModel()
        }

        if (!networkHelper.isNetworkConnected()) {
            return@withContext emptyList()
        }

        val response = safeApiCall {
            NasaApi.retrofitService.getAsteroidFeed(
                startDate = today,
                endDate = DateUtils.getDateNDaysFromToday(DEFAULT_END_DATE_DAYS)
            )
        }

        if (response != null && response.isSuccessful) {
            response.body()?.string()?.let { json ->
                val networkAsteroids = parseAsteroidsJsonResult(JSONObject(json))
                database.asteroidDao().insertAll(networkAsteroids.map { it.asDatabaseModel() })
                networkAsteroids
            } ?: emptyList()
        } else {
            emptyList()
        }
    }

    val cachedPictures: LiveData<List<PictureOfDayEntity>> = database.pictureDao().getPictures()

    suspend fun getPictureOfDay() = withContext(Dispatchers.IO) {

        val today = DateUtils.getToday()

        if (!networkHelper.isNetworkConnected()) {
            return@withContext cachedPictures.value
        }

        val picture = safeApiCall {
            NasaApi.retrofitService.getPictureOfDay()
        }
        if (picture != null && picture.mediaType == "image") {
            database.pictureDao().insert(
                PictureOfDayEntity(
                    date = today,
                    url = picture.url,
                    mediaType = picture.mediaType,
                    title = picture.title
                )
            )
        } else {
            Timber.w("Failed to fetch from API or media not image. Loading cached picture...")
            return@withContext cachedPictures.value
        }
    }

    suspend fun deleteOldAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao().deletePastAsteroids(DateUtils.getToday())
        }
    }
}