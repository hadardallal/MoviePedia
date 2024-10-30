package com.moviepedia.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.moviepedia.app.model.Movie
import com.moviepedia.app.room.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    val allMovies: LiveData<List<Movie>> = repository.allMovies.asLiveData()
    val allFavouriteMovies: LiveData<List<Movie>> = repository.allFavouriteMovies.asLiveData()

    fun getMovieById(id: Int): LiveData<Movie> {
        return repository.getMovieById(id)
    }

    fun insert(movie: Movie) = viewModelScope.launch {
        repository.insert(movie)
    }

    fun update(movie: Movie) = viewModelScope.launch {
        repository.update(movie)
    }

    fun delete(movie: Movie) = viewModelScope.launch {
        repository.delete(movie)
    }
}

class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}