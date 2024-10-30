package com.moviepedia.app.adapters

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moviepedia.app.BuildConfig
import com.moviepedia.app.R
import com.moviepedia.app.databinding.ItemMovieRowDesignBinding
import com.moviepedia.app.model.Movie
import java.io.File

class MovieListAdapter() : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(MovieDiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(movie: Movie,position: Int)
        fun onItemEditClick(movie: Movie,position: Int)
        fun onItemDeleteClick(movie: Movie,position: Int)
        fun onItemFavouriteClick(movie: Movie,position: Int)
    }

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemLayout = ItemMovieRowDesignBinding.inflate(LayoutInflater.from(parent.context))
        return MovieViewHolder(itemLayout, mListener!!)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bindData(movie, position)
    }

    class MovieViewHolder(
        private val binding: ItemMovieRowDesignBinding,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(movie: Movie, position: Int) {
            if (movie.photoUri!!.isEmpty()){
                binding.imageViewMovie.setImageResource(R.drawable.moviepedia)
            }
            else{
                val imgFile = File(movie.photoUri!!)
                if (imgFile.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    binding.imageViewMovie.setImageBitmap(myBitmap)
                }
            }
            binding.textViewTitle.text = movie.title
            binding.textViewDescription.text = movie.description
            binding.textViewGenre.text = movie.genre
            binding.textViewReleaseDate.text = movie.releaseDate
            binding.textViewRating.text = "${movie.rating}"

            if (movie.isFavourite){
                binding.favouriteImageView.setImageResource(R.drawable.ic_favourite)
            }
            else{
                binding.favouriteImageView.setImageResource(R.drawable.ic_favourite_border)
            }

            binding.root.setOnClickListener {
                listener.onItemClick(movie,position)
            }

            binding.editImageView.setOnClickListener {
                listener.onItemEditClick(movie,position)
            }

            binding.deleteImageView.setOnClickListener {
                listener.onItemDeleteClick(movie,position)
            }

            binding.favouriteImageView.setOnClickListener {
                listener.onItemFavouriteClick(movie,position)
            }
        }
    }

}

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}