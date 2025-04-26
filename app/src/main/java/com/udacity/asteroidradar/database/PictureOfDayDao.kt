package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PictureOfDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(picture: PictureOfDayEntity)

    @Query("SELECT * FROM picture_of_day_table ORDER BY date DESC")
    fun getPictures(): LiveData<List<PictureOfDayEntity>>
}
