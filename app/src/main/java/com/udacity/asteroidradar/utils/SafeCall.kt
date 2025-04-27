package com.udacity.asteroidradar.utils

import timber.log.Timber


suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): T? {
    return try {
        apiCall()
    } catch (e: Exception) {
        Timber.e(e, "safeApiCall error")
        null
    }
}