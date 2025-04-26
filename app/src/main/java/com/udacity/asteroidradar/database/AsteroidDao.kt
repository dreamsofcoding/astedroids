package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroids ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<AsteroidEntity>)

    @Query("DELETE FROM asteroids WHERE closeApproachDate < :today")
    suspend fun deletePastAsteroids(today: String)

    @Query("SELECT * FROM asteroids WHERE closeApproachDate >= :today ORDER BY closeApproachDate ASC")
    suspend fun getAsteroidsSync(today: String): List<AsteroidEntity>

}