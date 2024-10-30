package com.moviepedia.app.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moviepedia.app.model.Movie
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context, applicationScope: CoroutineScope): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_pedia_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}