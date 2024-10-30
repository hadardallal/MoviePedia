package com.moviepedia.app.view

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moviepedia.app.R
import com.moviepedia.app.databinding.FragmentItemDetailBinding
import com.moviepedia.app.model.Movie
import com.moviepedia.app.model.Movie.Companion.toMovie
import java.io.File

class ItemDetailFragment : Fragment() {

    private lateinit var binding:FragmentItemDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentItemDetailBinding.inflate(inflater, container, false)

       val movie = arguments?.toMovie()
        if (movie != null) {
            binding.titleView.text = movie.title
            binding.descriptionView.text = movie.description
            binding.genreView.text = movie.genre
            binding.releaseDateView.text = movie.releaseDate
            binding.ratingView.text = "${movie.rating}"
            if (movie.photoUri!!.isNotEmpty()){
                val imgFile = File(movie.photoUri!!)
                if (imgFile.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    binding.movieImageView.setImageBitmap(myBitmap)
                }
            }
        }

        return binding.root
    }

}