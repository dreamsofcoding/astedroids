package com.udacity.asteroidradar.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.udacity.asteroidradar.utils.Constants.API_QUERY_DATE_FORMAT
import java.util.Date
import java.util.Locale

object DateUtils {

    fun getToday(): String {
        val calendar = Calendar.getInstance()
        return formatDate(calendar.time)
    }

    /**
     * Returns a date N days from today, formatted for the API.
     */
    fun getDateNDaysFromToday(days: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return formatDate(calendar.time)
    }


    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(date)
    }
}