package com.moviepedia.app.room

import com.moviepedia.app.model.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieDao: MovieDao) {

    val allMovies: Flow<List<Movie>> = movieDao.getAllMovies()
    val allFavouriteMovies: Flow<List<Movie>> = movieDao.getAllFavouriteMovies()

    fun getMovieById(id: Int) = movieDao.getMovieById(id)

    suspend fun insert(movie: Movie) {
        movieDao.insert(movie)
    }

    suspend fun update(movie: Movie) {
        movieDao.update(movie)
    }

    suspend fun delete(movie: Movie) {
        movieDao.delete(movie)
    }
}