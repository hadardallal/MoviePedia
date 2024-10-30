package com.moviepedia.app.model

import android.os.Bundle
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var description: String,
    var isFavourite: Boolean,
    var photoUri: String?,
    var genre:String,
    var releaseDate: String,
    var rating: Float,
    var createdAt:Long = System.currentTimeMillis(),
    var updatedAt:Long = System.currentTimeMillis()
):Serializable{

    companion object{
        fun Movie.toBundle(): Bundle {
            return Bundle().apply {
                putInt("id", id)
                putString("title", title)
                putString("description", description)
                putBoolean("isFavourite",isFavourite)
                putString("photoUri",photoUri)
                putString("genre", genre)
                putString("releaseDate", releaseDate)
                putFloat("rating", rating)
            }
        }

        fun Bundle.toMovie(): Movie {
            return Movie(
                id = getInt("id"),
                title = getString("title") ?: "",
                description = getString("description") ?: "",
                isFavourite = getBoolean("isFavourite") ?: false,
                photoUri = getString("photoUri"),
                genre = getString("genre") ?: "",
                releaseDate = getString("releaseDate") ?: "",
                rating = getFloat("rating")
            )
        }
    }
}