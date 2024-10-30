package com.moviepedia.app.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moviepedia.app.R
import com.moviepedia.app.databinding.FragmentMediaBinding
import com.moviepedia.app.services.MediaService

class MediaFragment : Fragment() {

    private lateinit var binding:FragmentMediaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMediaBinding.inflate(inflater, container, false)

        binding.playButton.setOnClickListener {
            val mediaServiceIntent = Intent(requireActivity(), MediaService::class.java).apply {
                putExtra("action", "play")
            }
            requireActivity().startService(mediaServiceIntent)
        }

        binding.stopButton.setOnClickListener {
            val mediaServiceIntent = Intent(requireActivity(), MediaService::class.java).apply {
                putExtra("action", "stop")
            }
            requireActivity().startService(mediaServiceIntent)
        }

       return binding.root
    }

}