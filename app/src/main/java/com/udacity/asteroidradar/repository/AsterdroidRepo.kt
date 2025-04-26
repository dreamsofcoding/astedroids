package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.PictureOfDayEntity
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.utils.Constants.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(
    private val database: AsteroidDatabase
) {

    val asteroidsList = database.asteroidDao().getAsteroids()

    suspend fun getAsteroids() {
        return withContext(Dispatchers.IO) {

            val today = DateUtils.getToday()

            val localAsteroids = database.asteroidDao().getAsteroidsSync(today)

            if (localAsteroids.isNotEmpty()) {
                localAsteroids.asDomainModel()
            } else {

                val response = NasaApi.retrofitService.getAsteroidFeed(
                    startDate = today,
                    endDate = DateUtils.getDateNDaysFromToday(DEFAULT_END_DATE_DAYS),
                )

                if (response.isSuccessful) {
                    val json = response.body()?.string()
                    if (json != null) {
                        val networkListOfAsteroids = parseAsteroidsJsonResult(JSONObject(json))
                        database.asteroidDao()
                            .insertAll(networkListOfAsteroids.map { it.asDatabaseModel() })
                        networkListOfAsteroids
                    } else emptyList()
                } else {
                    emptyList()
                }
            }
        }
    }

    val cachedPictures: LiveData<List<PictureOfDayEntity>> = database.pictureDao().getPictures()

    suspend fun getPictureOfDay() {
        val picture = NasaApi.retrofitService.getPictureOfDay()
        if (picture.mediaType == "image") {
            database.pictureDao().insert(
                PictureOfDayEntity(
                    date = DateUtils.getToday(),
                    url = picture.url,
                    mediaType = picture.mediaType,
                    title = picture.title
                )
            )
        }
    }
}