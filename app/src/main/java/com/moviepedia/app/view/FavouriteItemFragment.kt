package com.moviepedia.app.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviepedia.app.App
import com.moviepedia.app.R
import com.moviepedia.app.adapters.MovieListAdapter
import com.moviepedia.app.databinding.FragmentFavouriteItemBinding
import com.moviepedia.app.model.Movie
import com.moviepedia.app.model.Movie.Companion.toBundle
import com.moviepedia.app.viewmodel.MovieViewModel
import com.moviepedia.app.viewmodel.MovieViewModelFactory

class FavouriteItemFragment : Fragment() {

    private lateinit var binding:FragmentFavouriteItemBinding
    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((requireActivity().application as App).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteItemBinding.inflate(inflater, container, false)

        val adapter = MovieListAdapter()

        binding.favouriteItemsRecyclerview.layoutManager = LinearLayoutManager(requireActivity())
        binding.favouriteItemsRecyclerview.adapter = adapter
        adapter.setOnItemClickListener(object : MovieListAdapter.OnItemClickListener{
            override fun onItemClick(movie: Movie, position: Int) {
                val bundle = movie.toBundle()
                findNavController().navigate(R.id.action_favouriteItem_to_itemDetailFragment, bundle)
            }

            override fun onItemEditClick(movie: Movie, position: Int) {
                val bundle = movie.toBundle()
                findNavController().navigate(R.id.action_favouriteItem_to_editItemFragment, bundle)
            }

            override fun onItemDeleteClick(movie: Movie, position: Int) {
                showDeleteConfirmationDialog(movie)
            }

            override fun onItemFavouriteClick(movie: Movie, position: Int) {
                movie.isFavourite = !movie.isFavourite
                movieViewModel.update(movie)
                if (movie.isFavourite){
                    Toast.makeText(requireContext(), R.string.favourite_added_message, Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(), R.string.favourite_removed_message, Toast.LENGTH_SHORT).show()
                }
                adapter.notifyDataSetChanged()
            }
        })

        movieViewModel.allFavouriteMovies.observe(viewLifecycleOwner, Observer { movies ->
            movies?.let { adapter.submitList(it) }
        })

        return binding.root
    }

    private fun showDeleteConfirmationDialog(movie: Movie) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(R.string.delete_movie_title_heading)
            setMessage(R.string.delete_movie_warning_message)
            setPositiveButton(R.string.yes) { _, _ ->
                movieViewModel.delete(movie)
                Toast.makeText(requireContext(), R.string.item_deleted_message, Toast.LENGTH_SHORT).show()
            }
            setNegativeButton(R.string.no, null)
        }.create().show()
    }

}