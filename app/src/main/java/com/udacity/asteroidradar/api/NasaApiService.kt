package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.utils.Constants
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidFeed(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Response<ResponseBody>

   @GET("planetary/apod")
    suspend fun getPictureOfDay(
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): PictureOfDay
}
