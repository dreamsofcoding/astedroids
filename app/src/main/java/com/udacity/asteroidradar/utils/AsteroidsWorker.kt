package com.udacity.asteroidradar.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import timber.log.Timber

class AsteroidsWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Timber.d("WorkManager Asteroid Worker started 🚀")
        val database = AsteroidDatabase.getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.deleteOldAsteroids()
            repository.getAsteroids()
            Timber.d("WorkManager Asteroid Worker finished successfully ✅")
            Result.success()
        } catch (e: Exception) {
            Timber.e("WorkManager Asteroid Worker failed ❌")
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "AstedroidsWorker"
    }
}