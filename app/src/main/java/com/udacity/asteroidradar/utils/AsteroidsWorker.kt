package com.udacity.asteroidradar.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository

class AsteroidsWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getDatabase(applicationContext)
        val networkHelper = NetworkHelper(applicationContext)
        val repository = AsteroidRepository(database, networkHelper)

        if (!networkHelper.isNetworkConnected()) {
            Result.failure()
        }

        return try {
            repository.deleteOldAsteroids()
            repository.getAsteroids()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "AstedroidsWorker"
    }
}