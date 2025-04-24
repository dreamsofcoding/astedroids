package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.Constants.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository {

    suspend fun getAsteroids(): List<Asteroid> {
        return withContext(Dispatchers.IO) {
            val response = NasaApi.retrofitService.getAsteroidFeed(
                startDate = DateUtils.getToday(),
                endDate = DateUtils.getDateNDaysFromToday(DEFAULT_END_DATE_DAYS),
                apiKey = Constants.API_KEY
            )

            if (response.isSuccessful) {
                val json = response.body()?.string()
                if (json != null) {
                    parseAsteroidsJsonResult(JSONObject(json))
                } else emptyList()
            } else {
                emptyList()
            }
        }
    }
}