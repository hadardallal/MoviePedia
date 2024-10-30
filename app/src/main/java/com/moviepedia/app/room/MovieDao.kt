package com.moviepedia.app.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moviepedia.app.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE isFavourite=1")
    fun getAllFavouriteMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieById(id: Int): LiveData<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)
}