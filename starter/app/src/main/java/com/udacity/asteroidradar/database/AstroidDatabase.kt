package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Asteroid


@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AstroidDatabase: RoomDatabase(){
    abstract val astroidDao: AsteroidDao

    companion object{
        @Volatile
        private var INSTANCE: AstroidDatabase? = null

        fun getInstance(context: Context) : AstroidDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AstroidDatabase::class.java,
                        "astroid_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }}

    }
}

