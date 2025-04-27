package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [AsteroidEntity::class, PictureOfDayEntity::class], version = 2, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract fun asteroidDao(): AsteroidDao
    abstract fun pictureDao(): PictureOfDayDao

    companion object {
        @Volatile
        private var INSTANCE: AsteroidDatabase? = null

        fun getDatabase(context: Context): AsteroidDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                    "asteroids"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS picture_of_day (
                url TEXT NOT NULL,
                media_type TEXT NOT NULL,
                title TEXT NOT NULL,
                date TEXT NOT NULL,
                PRIMARY KEY(date)
            )
        """.trimIndent())
    }
}