package com.moviepedia.app

import android.app.Application
import com.moviepedia.app.room.MovieDatabase
import com.moviepedia.app.room.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { MovieDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { MovieRepository(database.movieDao()) }
}