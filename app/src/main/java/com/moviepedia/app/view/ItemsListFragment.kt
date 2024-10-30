package com.moviepedia.app.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviepedia.app.App
import com.moviepedia.app.R
import com.moviepedia.app.adapters.MovieListAdapter
import com.moviepedia.app.databinding.FragmentItemsListBinding
import com.moviepedia.app.model.Movie
import com.moviepedia.app.model.Movie.Companion.toBundle
import com.moviepedia.app.viewmodel.MovieViewModel
import com.moviepedia.app.viewmodel.MovieViewModelFactory
import java.io.File

class ItemsListFragment : Fragment() {

    private lateinit var binding:FragmentItemsListBinding
    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((requireActivity().application as App).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =  FragmentItemsListBinding.inflate(inflater, container, false)

        val adapter = MovieListAdapter()

        binding.movieItemsRecyclerview.layoutManager = LinearLayoutManager(requireActivity())
        binding.movieItemsRecyclerview.adapter = adapter
        adapter.setOnItemClickListener(object :MovieListAdapter.OnItemClickListener{
            override fun onItemClick(movie: Movie,position: Int) {
                val bundle = movie.toBundle()
                findNavController().navigate(R.id.action_itemsFragment_to_itemDetailFragment, bundle)
            }

            override fun onItemEditClick(movie: Movie,position: Int) {
                val bundle = movie.toBundle()
                findNavController().navigate(R.id.action_itemsFragment_to_editItemFragment, bundle)
            }

            override fun onItemDeleteClick(movie: Movie,position: Int) {
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

        movieViewModel.allMovies.observe(viewLifecycleOwner, Observer { movies ->
            movies?.let { adapter.submitList(it) }
        })

        binding.addItemFab.setOnClickListener {
            findNavController().navigate(R.id.action_itemsFragment_to_addItemFragment)
        }



        return binding.root
    }


    private fun showDeleteConfirmationDialog(movie: Movie) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(R.string.delete_movie_title_heading)
            setMessage(R.string.delete_movie_warning_message)
            setPositiveButton(R.string.yes) { _, _ ->
                movieViewModel.delete(movie)
                if (movie.photoUri!!.isNotEmpty()){
                    val file = File(movie.photoUri!!)
                    if (file.exists()){
                        file.delete()
                    }
                }
                Toast.makeText(requireContext(), R.string.item_deleted_message, Toast.LENGTH_SHORT).show()
            }
            setNegativeButton(R.string.no, null)
        }.create().show()
    }
}