package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_of_day_table")
data class PictureOfDayEntity(
    @PrimaryKey
    val date: String,
    val url: String,
    val mediaType: String,
    val title: String
)